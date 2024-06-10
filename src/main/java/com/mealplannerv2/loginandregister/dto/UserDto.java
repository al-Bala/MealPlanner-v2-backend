package com.mealplannerv2.loginandregister.dto;

import com.mealplannerv2.plangenerator.infrastructure.controller.dto.SavedPreferences;
import lombok.Builder;

@Builder
public record UserDto(
        String username,
        String email,
        String password,
        SavedPreferences preferences
) {
}
