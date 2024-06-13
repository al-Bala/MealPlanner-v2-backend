package com.mealplannerv2.plangenerator.infrastructure.controller.dto.meal;

import lombok.Getter;

@Getter
public class Meal {

    private final MealType mealType;
    private final String name;
    private final int priority;
    private final int timeMin;

    public Meal(String name, int timeMin) {
        if(name.equals(MealType.BREAKFAST.name())){
            this.mealType = MealType.BREAKFAST;
        } else if(name.equals(MealType.DINNER.name())){
            this.mealType = MealType.DINNER;
        }else {//if(name.equals(MealType.SUPPER.name())){
            this.mealType = MealType.SUPPER;
        }

        this.name = mealType.name();
        this.priority = mealType.getPriority();
        this.timeMin = timeMin;
    }

//    public Meal(MealType mealType, int timeMin) {
//        this.mealType = mealType;
//        this.name = mealType.name();
//        this.priority = mealType.getPriority();
//        this.timeMin = timeMin;
//    }

//    public Meal createMeal(String name){
//        if(name.equals(MealType.BREAKFAST.name())){
//            return new Meal(MealType.BREAKFAST, timeMin);
//        } else {//if(name.equals(MealType.SUPPER.name())){
//            return new Meal(MealType.SUPPER, timeMin);
//        }
//    }
}
