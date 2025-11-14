package com.orm.learn_orm.my_tests.workbook_test.custom_annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a field (which should be a List) as a nested collection
 * of DTOs to be exported as grouped child rows.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExportNested {
    Class<?>[] groups() default {};
}
