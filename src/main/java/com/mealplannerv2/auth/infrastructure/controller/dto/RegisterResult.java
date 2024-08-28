package com.mealplannerv2.auth.infrastructure.controller.dto;

import com.mealplannerv2.auth.infrastructure.controller.error.RegisterErrors;

public record RegisterResult(
        RegisterErrors errors,
        boolean created,
        String username
) {
}
