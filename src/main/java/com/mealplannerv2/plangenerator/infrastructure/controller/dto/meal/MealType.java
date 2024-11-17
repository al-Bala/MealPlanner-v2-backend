package com.mealplannerv2.plangenerator.infrastructure.controller.dto.meal;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
public enum MealType {
    BREAKFAST("BREAKFAST-ID", "Breakfast", 2),
    DINNER("DINNER-ID", "Dinner", 1),
    SUPPER("SUPPER-ID", "Supper", 3);

    private final String id;
    private final String name;
    private final int priority;

    MealType(String id, String name, int priority) {
        this.id = id;
        this.name = name;
        this.priority = priority;
    }

    public static MealType getById(String id) {
        Optional<MealType> any = Arrays.stream(values())
                .filter(value -> value.id.equals(id))
                .findAny();
        return any.orElse(null);
    }
}
