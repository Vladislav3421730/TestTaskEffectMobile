package com.example.testtaskeffectmobile.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = BlockStatusValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface BlockStatus {
    String message() default "Block status must be in 'CREATED','COMPLETED','REJECTED'";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
