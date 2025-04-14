package com.example.testtaskeffectmobile.validation;

import com.example.testtaskeffectmobile.model.enums.BlockStatusType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class BlockStatusValidator implements ConstraintValidator<BlockStatus, String> {
    @Override
    public boolean isValid(String str, ConstraintValidatorContext constraintValidatorContext) {
        return Arrays.stream(BlockStatusType.values()).anyMatch(status -> str.equals(status.name()));
    }
}
