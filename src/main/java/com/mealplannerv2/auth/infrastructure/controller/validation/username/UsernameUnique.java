package com.mealplannerv2.auth.infrastructure.controller.validation.username;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UsernameUniqueValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface UsernameUnique {

    String message() default "{username.repeated}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
