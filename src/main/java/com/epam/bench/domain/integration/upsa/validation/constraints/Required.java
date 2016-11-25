package com.epam.bench.domain.integration.upsa.validation.constraints;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.OverridesAttribute;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;

/**
 * Created by Tetiana_Antonenko1
 */
@NotNull
@Documented
@Constraint(
    validatedBy = {RequiredValidator.class}
)
@Target({ ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Required {
    @OverridesAttribute(
        constraint = NotNull.class,
        name = "message"
    )
    String message() default "{validation.error.default.required.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
