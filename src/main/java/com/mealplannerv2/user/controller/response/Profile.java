package com.mealplannerv2.user.controller.response;

import com.mealplannerv2.user.model.Plan;
import lombok.Builder;

import java.util.List;

@Builder
public record Profile(
        String username,
        List<Plan> plans
) {
}
