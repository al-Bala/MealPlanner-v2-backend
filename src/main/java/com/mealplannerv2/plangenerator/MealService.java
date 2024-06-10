package com.mealplannerv2.plangenerator;

import com.mealplannerv2.plangenerator.infrastructure.controller.dto.meal.Dinner;
import com.mealplannerv2.plangenerator.infrastructure.controller.dto.meal.Meal;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
class MealService {

    public List<Meal> sortMealsByPriority(List<Meal> meals){
        return meals.stream()
                .sorted(Comparator.comparingInt(Meal::getPriority))
                .toList();
    }

    public int getForHowManyDays(Meal meal){
        if(meal instanceof Dinner){
            return ((Dinner) meal).isFor2Days() ? 2 : 1;
        } else
            return 1;
    }

}
