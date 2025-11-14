package com.orm.learn_orm.exception;

import java.util.List;
import java.util.Map;

public class ParsingException extends RuntimeException {
    private final List<Map<String, String>> errorDetails;

    public ParsingException(String message, List<Map<String, String>> errorDetails) {
        super(message);
        this.errorDetails = errorDetails;
    }
}
