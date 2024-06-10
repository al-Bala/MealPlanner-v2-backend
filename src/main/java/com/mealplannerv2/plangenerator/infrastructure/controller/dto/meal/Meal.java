package com.mealplannerv2.plangenerator.infrastructure.controller.dto.meal;

import lombok.Getter;

@Getter
public abstract class Meal {

    private final MealType mealType;
    private final String name;
    private final int priority;
    private final int timeMin;

    public Meal(MealType mealType, int timeMin) {
        this.mealType = mealType;
        this.name = mealType.name();
        this.priority = mealType.getPriority();
        this.timeMin = timeMin;
    }

    public abstract Meal createMeal();
}
