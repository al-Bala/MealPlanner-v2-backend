package com.mealplannerv2.plangenerator.infrastructure.controller.dto.meal;

import lombok.Getter;

@Getter
public class Dinner extends Meal{

    private final boolean for2Days;

    public Dinner(String name, int timeMin, boolean for2Days) {
        super(name, timeMin);
        this.for2Days = for2Days;
    }

//    public Dinner(MealType mealType, int timeMin, boolean for2Days) {
//        super(mealType, timeMin);
//        this.for2Days = for2Days;
//    }

//    @Override
//    public Dinner createMeal(String name){
//        if(name.equals(MealType.DINNER.name())){
//            return new Dinner(MealType.DINNER, getTimeMin(), for2Days);
//        }
//        return null;
//    }

//    @Override
//    public Meal createMeal() {
//        return new Dinner(getMealType(), getTimeMin(), for2Days);
//    }
}
