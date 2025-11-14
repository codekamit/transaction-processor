package com.orm.learn_orm.my_tests.workbook_test.custom_annotation;

import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class ExportableAnnotationProcessor {

    private static final Logger LOGGER = LogManager.getLogger(ExportableAnnotationProcessor.class);
    private static final String INVALID_EXCEL_COLUMN_USAGE_PARAMETER_EXP = "Invalid @ExportColumn on method '%s': Method must not have any parameters.";
    private static final String INVALID_EXCEL_COLUMN_NO_RETURN_EXP = "Invalid @ExportColumn on method '%s': Method must return a value (not void).";
    private static final String GENERIC_FIELD_EXP = "Error getting generic type for field: {} | Error: {}";

    /**
     * Public helper class to store a Field or Method to be processed.
     */
    public static class ProcessedColumn {
        public final Object member;
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

    /**
     * Retrieves and filters the exportable members (fields and methods) based on the active export groups.
     *
     * @param clazz The DTO class to analyze.
     * @param activeGroups The marker interfaces representing the current export context (e.g., ClientReport.class).
     * @return A sorted list of members to be included in the export.
     */
    public List<ProcessedColumn> getOrderedProcessedFields(Class<?> clazz, Class<?>... activeGroups) {
        List<ProcessedColumn> membersList = new ArrayList<>();
        Set<Class<?>> activeGroupSet = new HashSet<>(Arrays.asList(activeGroups));

        Set<String> excludedFields = new HashSet<>();

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

                    if (shouldInclude(annotation.groups(), activeGroupSet)) {
                        continue;
                    }

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
                                INVALID_EXCEL_COLUMN_USAGE_PARAMETER_EXP, method.getName()
                        ));
                    }
                    if (method.getReturnType() == void.class) {
                        throw new RuntimeException(String.format(
                                INVALID_EXCEL_COLUMN_NO_RETURN_EXP, method.getName()
                        ));
                    }

                    ExportColumn annotation = method.getAnnotation(ExportColumn.class);

                    if (shouldInclude(annotation.groups(), activeGroupSet)) {
                        continue;
                    }

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

    /**
     * Core logic to determine if a member should be included.
     * NEW, FIXED LOGIC
     */
    private boolean shouldInclude(Class<?>[] fieldGroups, Set<Class<?>> activeGroups) {
        if (fieldGroups.length == 0) {
            return false;
        }

        if (activeGroups.isEmpty()) {
            return true;
        }

        for (Class<?> fieldGroup : fieldGroups) {
            if (activeGroups.contains(fieldGroup)) {
                return false;
            }
        }

        return true;
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
            LOGGER.error(GENERIC_FIELD_EXP, listField.getName(), e.getMessage());
            return null;
        }
        return null;
    }
}
