package com.mealplannerv2.user.dto;

import com.mealplannerv2.recipe.History;
import lombok.Builder;

@Builder
public record Profile(
        String username,
        History history
) {
}
