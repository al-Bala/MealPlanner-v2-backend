package com.mealplannerv2.plangenerator.infrastructure.controller.dto.meal;

import com.mealplannerv2.plangenerator.infrastructure.controller.dto.MealValues;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.mealplannerv2.plangenerator.infrastructure.controller.dto.meal.MealType.*;

@Log4j2
@Getter
public class Meal {

    private final static int ONE_DAY = 1;

    private final MealType mealType;
    private final String name;
    private final int priority;
    private final Integer timeMin;
    private final int forHowManyDays;

    public Meal(MealType mealType, Integer timeMin, int forHowManyDays) {
        this.mealType = mealType;
        this.name = mealType.name();
        this.priority = mealType.getPriority();
        this.timeMin = timeMin;
        this.forHowManyDays = forHowManyDays;
    }

    public static List<Meal> getAllMeals(List<MealValues> mealsValues) {
        List<Meal> meals  = new ArrayList<>();
        for(MealValues mealVal: mealsValues){
            Meal meal = getMeal(mealVal);
            meals.add(meal);
        }
        return sortMealsByPriority(meals);
    }

    private static Meal getMeal(MealValues mealVal){
        if(isNamesEquals(mealVal, BREAKFAST)){
            return new Meal(BREAKFAST, mealVal.timeMin(), ONE_DAY);
        } else if(isNamesEquals(mealVal, DINNER)){
            return new Meal(DINNER, mealVal.timeMin(), mealVal.forHowManyDays());
        } else if(isNamesEquals(mealVal, SUPPER)){
            return new Meal(SUPPER, mealVal.timeMin(), ONE_DAY);
        }
        log.error("Wrong meal name.");
        return null;
    }

    private static boolean isNamesEquals(MealValues mealVal, MealType mealType) {
        return mealVal.mealId().equals(mealType.name());
    }

    private static List<Meal> sortMealsByPriority(List<Meal> meals){
        return meals.stream()
                .sorted(Comparator.comparingInt(Meal::getPriority))
                .toList();
    }
}
