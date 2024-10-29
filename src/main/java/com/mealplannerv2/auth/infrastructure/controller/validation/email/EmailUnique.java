package com.mealplannerv2.auth.infrastructure.controller.validation.email;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EmailUniqueValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface EmailUnique {

    String message() default "{email.repeated}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
