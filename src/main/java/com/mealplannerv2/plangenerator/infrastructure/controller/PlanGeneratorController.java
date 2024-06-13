package com.mealplannerv2.plangenerator.infrastructure.controller;

import com.mealplannerv2.loginandregister.LoginAndRegisterFacade;
import com.mealplannerv2.plangenerator.DataForRecipeFiltering;
import com.mealplannerv2.plangenerator.PlanGeneratorFacade;
import com.mealplannerv2.plangenerator.PlannedDay;
import com.mealplannerv2.plangenerator.infrastructure.controller.dto.*;
import com.mealplannerv2.plangenerator.infrastructure.controller.dto.meal.Dinner;
import com.mealplannerv2.plangenerator.infrastructure.controller.dto.meal.Meal;
import com.mealplannerv2.plangenerator.infrastructure.controller.dto.meal.MealType;
//import com.mealplannerv2.plangenerator.infrastructure.controller.dto.meal.OtherMeals;
import com.mealplannerv2.plangenerator.recipefilter.RecipeFetcherFacade;
import com.mealplannerv2.plangenerator.recipefilter.dto.RecipeDto;
import com.mealplannerv2.plangenerator.recipefilter.model.Ingredient;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/plan")
class PlanGeneratorController {

    private final PlanGeneratorFacade planGeneratorFacade;
    private final RecipeFetcherFacade recipeFetcherFacade;
    private final LoginAndRegisterFacade loginAndRegisterFacade;

//    @GetMapping("/preferences")
//    public ResponseEntity<SavedPreferences> getPreferencesForm(){
//        // if data is saved in database
//        // return diet, portions and productsToAvoid
//        UserDto user = loginAndRegisterFacade.getAuthenticatedUser();
//        return ResponseEntity.ok(user.preferences());
//    }

    @PostMapping("/preferences")
    public ResponseEntity<List<PlannedDay>> postInfo(@RequestBody PreferencesAndDayInfo preferencesAndDayInfo){
        // if data not saved,
        // save diet, portions and productsToAvoid in database for user
//        UserDto user = loginAndRegisterFacade.getAuthenticatedUser();
//        if(user.preferences() == null){
//        }

        DayInfoDto dayInfoDto = new DayInfoDto(new ArrayList<>());
        for(MealInfo meal: preferencesAndDayInfo.dayInfo().mealsInfo()){
            Meal meal2 = new Meal(meal.mealName(), meal.timeMin());
            dayInfoDto.meals().add(meal2);
        }

        List<PlannedDay> firstDayOfPlan = planGeneratorFacade.createFirstDayOfPlan(preferencesAndDayInfo.preferencesInfo(), dayInfoDto);

        return ResponseEntity.ok(firstDayOfPlan);
    }

    @GetMapping("")
    public ResponseEntity<PlanResponseDto> find() {
        DataForRecipeFiltering info = DataForRecipeFiltering.builder()
                .forHowManyDays(1)
//                .diet("wegetariańska")
//                .timeForPrepareMin(5)
//                .ingredientsToUseFirstly(List.of("marchew"))
//                .productsToAvoid(List.of("oliwki"))
                .ingredientsToUseFirstly(List.of(
                        new Ingredient("truskawki", 100.0, ""),
                        new Ingredient("marchew", 2.0, ""),
                        new Ingredient("ryż", 300.0, "")))
                .build();

        RecipeDto result = recipeFetcherFacade.fetchRecipeByPreferences(info);
        System.out.println(result);
        return ResponseEntity.ok(new PlanResponseDto(result));
    }

}
