package com.mealplannerv2.plangenerator.infrastructure.controller.dto.meal;

import com.mealplannerv2.plangenerator.infrastructure.controller.dto.request.PanelValuesForMeal;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.mealplannerv2.plangenerator.infrastructure.controller.dto.meal.MealType.*;

@Log4j2
@Getter
public class MealWithType {
    private final MealType mealType;
    private final Integer timeMin;
    private final int forHowManyDays;

    public MealWithType(MealType mealType, Integer timeMin, int forHowManyDays) {
        this.mealType = mealType;
        this.timeMin = timeMin;
        this.forHowManyDays = forHowManyDays;
    }

    public static List<MealWithType> setTypeForAll(List<PanelValuesForMeal> panelValuesForAllMeals) {
        List<MealWithType> allMealsWithType = new ArrayList<>();
        for(PanelValuesForMeal mealPanelVal: panelValuesForAllMeals){
            MealWithType mealWithType = setType(mealPanelVal);
            allMealsWithType.add(mealWithType);
        }
        return allMealsWithType;
    }

    private static MealWithType setType(PanelValuesForMeal mealPanelVal){
        MealType mealType = getById(mealPanelVal.mealId());
        if(mealType != null){
            return new MealWithType(mealType, mealPanelVal.timeMin(), mealPanelVal.forHowManyDays());
        } else {
            log.error("Wrong meal id");
            return null;
        }
    }

    public static List<MealWithType> sortByPriority(List<MealWithType> allMealsWithType){
        return allMealsWithType.stream()
                .sorted(Comparator.comparingInt(m -> m.getMealType().getPriority()))
                .toList();
    }
}
