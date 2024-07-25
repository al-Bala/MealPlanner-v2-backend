package com.mealplannerv2.product;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "units")
public record Unit(
        @Id String id,
        String label,
        List<String> units
) {
}
