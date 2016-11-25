package com.epam.bench.domain.integration.upsa.validation.constraints;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Tetiana_Antonenko1
 */
@Target({ ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ErrorMessageAnnotation {
    String value();
}

