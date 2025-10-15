package com.orm.learn_orm.file_management;

import io.micrometer.common.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public interface FileFieldSetter {

    static void processField(List<String> row, Map<String, Integer> headerMap, String fieldName,
                             Map<String, String> errorMap, Consumer<String> setter) {
        String value = row.get(headerMap.get(fieldName));
        if (StringUtils.isBlank(value)) {
            errorMap.put(fieldName, "Field is required");
        } else {
            setter.accept(value);
        }
    }

    static void processNumericField(List<String> row, Map<String, Integer> headerMap, String fieldName,
                                    Map<String, String> errorMap, Consumer<Double> setter) {
        Double numericValue = null;
        try {
            String value = row.get(headerMap.get(fieldName));
            numericValue = Double.valueOf(value);
        } catch (NumberFormatException ex) {
            errorMap.put(fieldName, "Invalid numeric value");
            return;
        }

        setter.accept(numericValue);
    }
}
