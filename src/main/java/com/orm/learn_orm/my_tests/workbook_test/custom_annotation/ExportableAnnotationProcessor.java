package com.orm.learn_orm.my_tests.workbook_test.custom_annotation;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Shared utility class to process annotations for any export format.
 * It uses reflection to find all headers and fields to be exported.
 */

@Service
@AllArgsConstructor
public class ExportableAnnotationProcessor {

    /**
     * Public helper class to store a Field or Method to be processed.
     */
    public static class ProcessedColumn {
        public final Object member; // Will be a Field or a Method
        public final String headerName;
        public final int order;

        public ProcessedColumn(Object member, String headerName, int order) {
            this.member = member;
            this.headerName = headerName;
            this.order = order;

            if (member instanceof Field) {
                ((Field) member).setAccessible(true);
            } else if (member instanceof Method) {
                ((Method) member).setAccessible(true);
            }
        }

        public Object getValue(Object obj) throws Exception {
            if (member instanceof Field) {
                return ((Field) member).get(obj);
            } else if (member instanceof Method) {
                // invoke with no arguments
                return ((Method) member).invoke(obj);
            }
            return null;
        }
    }

    public String convertCamelCaseToTitleCase(String camelCase) {
        if (camelCase == null || camelCase.isEmpty()) {
            return "";
        }
        Pattern pattern = Pattern.compile("(?<=[a-z])(?=[A-Z])|(?<=[A-Z])(?=[A-Z][a-z])");
        Matcher matcher = pattern.matcher(camelCase);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, " " + matcher.group());
        }
        matcher.appendTail(sb);
        String titleCase = sb.toString();
        return Character.toUpperCase(titleCase.charAt(0)) + titleCase.substring(1);
    }

    public String convertGetterToTitleCase(String methodName) {
        String fieldName = methodName;
        if (fieldName.startsWith("get") && fieldName.length() > 3) {
            fieldName = fieldName.substring(3, 4).toLowerCase() + fieldName.substring(4);
        } else if (fieldName.startsWith("is") && fieldName.length() > 2) {
            fieldName = fieldName.substring(2, 3).toLowerCase() + fieldName.substring(3);
        }
        return convertCamelCaseToTitleCase(fieldName);
    }

    public List<ProcessedColumn> getOrderedProcessedFields(Class<?> clazz) {
        List<ProcessedColumn> membersList = new ArrayList<>();

        Set<String> excludedFields = new HashSet<>();
        if (clazz.isAnnotationPresent(ExportIgnore.class)) {
            ExportIgnore ignoreAnnotation = clazz.getAnnotation(ExportIgnore.class);
            excludedFields.addAll(Arrays.asList(ignoreAnnotation.excludes()));
        }

        Stack<Class<?>> classStack = new Stack<>();
        Class<?> current = clazz;
        while (current != null && current != Object.class) {
            classStack.push(current);
            current = current.getSuperclass();
        }

        while (!classStack.isEmpty()) {
            Class<?> c = classStack.pop();

            for (Field field : c.getDeclaredFields()) {
                if (excludedFields.contains(field.getName()) ||
                        field.isAnnotationPresent(ExportNested.class) ||
                        java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
                    continue;
                }

                if (field.isAnnotationPresent(ExportColumn.class)) {
                    ExportColumn annotation = field.getAnnotation(ExportColumn.class);
                    int order = annotation.order();
                    String headerName;
                    if (annotation.name().isEmpty()) {
                        headerName = convertCamelCaseToTitleCase(field.getName());
                    } else {
                        headerName = annotation.name();
                    }
                    membersList.add(new ProcessedColumn(field, headerName, order));
                }
            }

            for (Method method : c.getDeclaredMethods()) {
                if (method.isAnnotationPresent(ExportColumn.class)) {

                    if (method.getParameterCount() != 0) {
                        throw new RuntimeException(String.format(
                                "Invalid @ExcelColumn on method '%s': Method must not have any parameters.", method.getName()
                        ));
                    }
                    if (method.getReturnType() == void.class) {
                        throw new RuntimeException(String.format(
                                "Invalid @ExcelColumn on method '%s': Method must return a value (not void).", method.getName()
                        ));
                    }

                    ExportColumn annotation = method.getAnnotation(ExportColumn.class);
                    int order = annotation.order();
                    String headerName;
                    if (annotation.name().isEmpty()) {
                        headerName = convertGetterToTitleCase(method.getName());
                    } else {
                        headerName = annotation.name();
                    }
                    membersList.add(new ProcessedColumn(method, headerName, order));
                }
            }
        }

        membersList.sort(Comparator.comparingInt(pc -> pc.order));
        return membersList;
    }

    public Field getFieldByAnnotation(Class<?> clazz, Class<? extends java.lang.annotation.Annotation> annotationClass) {
        Class<?> current = clazz;
        while (current != null && current != Object.class) {
            for (Field field : current.getDeclaredFields()) {
                if (field.isAnnotationPresent(annotationClass)) {
                    return field;
                }
            }
            current = current.getSuperclass();
        }
        return null;
    }

    public Class<?> getGenericTypeOfList(Field listField) {
        try {
            Type genericType = listField.getGenericType();
            if (genericType instanceof ParameterizedType) {
                ParameterizedType listType = (ParameterizedType) genericType;
                Type[] typeArguments = listType.getActualTypeArguments();
                if (typeArguments.length > 0) {
                    return (Class<?>) typeArguments[0];
                }
            }
        } catch (Exception e) {
            System.err.println("Error getting generic type for field: " + listField.getName() + " | Error: " + e.getMessage());
            return null;
        }
        return null;
    }
}

