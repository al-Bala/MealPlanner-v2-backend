package com.mealplannerv2.plangenerator.infrastructure.controller.dto.meal;

import lombok.Getter;

@Getter
public enum MealType {
    BREAKFAST(20),
    DINNER(10),
    SUPPER(30);

    private final int priority;

    MealType(int priority){
        this.priority = priority;
    }
}
