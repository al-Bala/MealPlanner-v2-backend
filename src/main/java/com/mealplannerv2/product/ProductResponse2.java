package com.mealplannerv2.product;

import lombok.Builder;

import java.util.List;

@Builder
public record ProductResponse2(
        List<ProductResponse> list
) {
}
