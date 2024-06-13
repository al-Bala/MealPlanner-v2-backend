package com.mealplannerv2.product;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Builder
@Document(collection = "products")
public record Product(
        @Id String id,
        String name,
        List<String> packing_units,
        String main_unit,
        Integer standard_weight,
        List<Integer> packing_sizes,
        Integer max_days_after_opening,
        Integer expireDays
) {
}
