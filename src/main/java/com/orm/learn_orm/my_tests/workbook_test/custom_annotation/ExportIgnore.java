package com.orm.learn_orm.my_tests.workbook_test.custom_annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Class-level annotation to exclude specific fields by name from Excel export.
 * This is an alternative to annotating every other field with @ExcelColumn.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE) // <-- Targets the CLASS
public @interface ExportIgnore {
    /**
     * An array of field names (as strings) to exclude from the export.
     */
    String[] excludes() default {};
}