package com.epam.bench.domain.integration.upsa.validation.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by Tetiana_Antonenko1
 */
public class RequiredValidator implements ConstraintValidator<Required, Object> {
    public RequiredValidator() {
    }

    public void initialize(Required required) {
    }

    public boolean isValid(Object object, ConstraintValidatorContext context) {
        boolean validObject = true;
        if(object != null) {
            validObject = false;
            if(object.toString().trim().length() > 0) {
                validObject = true;
            }
        }

        return validObject;
    }
}
