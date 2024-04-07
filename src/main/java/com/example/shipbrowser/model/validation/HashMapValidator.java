package com.example.shipbrowser.model.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.Map;

public class HashMapValidator implements ConstraintValidator<MapValidation, Map<String, String>> {

    private String[] allowedKeys;
    private String[] valueRestriction;

    @Override
    public boolean isValid(Map<String, String> value, ConstraintValidatorContext context) {
        if(value == null){
            return true;
        }
        return value.entrySet().stream().allMatch((keyset -> {
            boolean keysAreValid = Arrays.stream(allowedKeys).anyMatch((allowedKey) -> keyset.getKey().equals(allowedKey));
            if (!keysAreValid) {
                return false;
            }
            if (valueRestriction.length == 0) {
                return true;
            }
            return Arrays.stream(valueRestriction).anyMatch((restriction) -> keyset.getValue().equals(restriction));
        }));
    }

    @Override
    public void initialize(MapValidation annotation) {
        allowedKeys = annotation.allowedKeys();
        valueRestriction = annotation.valueRestriction();
    }
}
