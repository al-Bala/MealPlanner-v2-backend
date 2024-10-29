package com.mealplannerv2.auth.infrastructure.controller.dto;

import com.mealplannerv2.auth.infrastructure.controller.validation.email.EmailUnique;
import com.mealplannerv2.auth.infrastructure.controller.validation.password.Password;
import com.mealplannerv2.auth.infrastructure.controller.validation.username.UsernameUnique;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(

        @UsernameUnique
        @NotBlank (message = "{username.require}")
        String username,

        @Email (message = "{email.invalid}")
        @EmailUnique
        @NotBlank (message = "{email.require}")
        String email,

        @Password
        @NotBlank (message = "{password.require}")
        String password
) {
}
