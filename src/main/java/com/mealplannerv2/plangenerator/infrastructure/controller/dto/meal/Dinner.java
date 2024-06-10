package com.mealplannerv2.plangenerator.infrastructure.controller.dto.meal;

import lombok.Getter;

@Getter
public class Dinner extends Meal{

    private final boolean for2Days;

    public Dinner(MealType mealType, int timeMin, boolean for2Days) {
        super(mealType, timeMin);
        this.for2Days = for2Days;
    }

    @Override
    public Meal createMeal() {
        return new Dinner(getMealType(), getTimeMin(), for2Days);
    }
}
