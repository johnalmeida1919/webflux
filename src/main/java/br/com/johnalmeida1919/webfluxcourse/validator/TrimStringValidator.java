package br.com.johnalmeida1919.webfluxcourse.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import static java.util.Objects.isNull;

public class TrimStringValidator implements ConstraintValidator<TrimString, String> {
    @Override
    public void initialize(TrimString constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return isNull(value) || value.trim().length() == value.length();
    }
}
