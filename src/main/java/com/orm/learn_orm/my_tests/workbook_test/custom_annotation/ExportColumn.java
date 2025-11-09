package com.orm.learn_orm.my_tests.workbook_test.custom_annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD}) // <-- ADDED ElementType.METHOD
public @interface ExportColumn {
    String name() default "";
    int order() default Integer.MAX_VALUE;
}