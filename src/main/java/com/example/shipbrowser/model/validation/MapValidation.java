package com.example.shipbrowser.model.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Constraint(validatedBy = HashMapValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface MapValidation {
    String message() default "Map criteria is not valid";

    String[] allowedKeys();

    String[] valueRestriction();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
