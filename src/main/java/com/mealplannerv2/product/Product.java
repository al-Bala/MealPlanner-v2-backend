package com.mealplannerv2.product;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Builder
@Document(collection = "products")
public record Product(
        @Id String id,
        String name,
        List<String> packingUnits,
        String mainUnit,
        List<Integer> packingMeasures,
        Integer maxDaysAfterOpening,
        Integer expireDays
) {
}
