package com.mealplannerv2.product;

import lombok.Builder;

import java.util.List;

@Builder
public record ProductResponse(
        String id,
        String name,
        List<String> packingUnits,
        String mainUnit,
        Integer standardWeight
) {
}
