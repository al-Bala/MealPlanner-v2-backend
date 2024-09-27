package com.mealplannerv2.user.dto;

import com.mealplannerv2.recipe.Plan;
import lombok.Builder;

import java.util.List;

@Builder
public record Profile(
        String username,
        List<Plan> plans
) {
}
