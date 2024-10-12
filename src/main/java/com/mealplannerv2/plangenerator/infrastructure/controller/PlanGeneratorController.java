package com.mealplannerv2.plangenerator.infrastructure.controller;

import com.mealplannerv2.plangenerator.PlanGeneratorFacade;
import com.mealplannerv2.plangenerator.infrastructure.controller.dto.request.MealValues;
import com.mealplannerv2.plangenerator.infrastructure.controller.dto.PreferencesDto;
import com.mealplannerv2.plangenerator.infrastructure.controller.dto.response.CreateDayResponse;
import com.mealplannerv2.user.model.SavedPrefers;
import com.mealplannerv2.plangenerator.infrastructure.controller.dto.request.AcceptDayRequest;
import com.mealplannerv2.plangenerator.infrastructure.controller.dto.request.ChangeDayRequest;
import com.mealplannerv2.plangenerator.infrastructure.controller.dto.request.FirstDayRequest;
import com.mealplannerv2.plangenerator.infrastructure.controller.dto.request.NextDayRequest;
import com.mealplannerv2.plangenerator.infrastructure.controller.dto.response.DayResult;
import com.mealplannerv2.storage.IngredientDto;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/generator")
class PlanGeneratorController {

    private final static String SUCCESS_MSG_MATCHING_RECIPE = "Successfully found all recipes.";
    private final PlanGeneratorFacade planGeneratorFacade;

    @PostMapping("/days/first")
    public ResponseEntity<CreateDayResponse> createFirstDayOfPlan(@RequestBody FirstDayRequest firstDayRequest) {
        PreferencesDto preferencesDto = getPreferences(firstDayRequest.savedPrefers(), firstDayRequest.mealsValues());
        List<IngredientDto> userIngs = firstDayRequest.userProducts();
        DayResult firstDayOfPlan = planGeneratorFacade.createFirstDayOfPlan(preferencesDto, userIngs);
        System.out.println("RESULT: " + firstDayOfPlan);
        return ResponseEntity.ok(new CreateDayResponse(SUCCESS_MSG_MATCHING_RECIPE, firstDayOfPlan));
    }

    @PostMapping("/days/next")
    public ResponseEntity<CreateDayResponse> createNextDayOfPlan(@RequestBody NextDayRequest nextDayRequest){
        PreferencesDto preferencesDto = getPreferences(nextDayRequest.savedPrefers(), nextDayRequest.mealsValues());
        DayResult nextDayOfPlan = planGeneratorFacade.createNextDayOfPlan(preferencesDto, nextDayRequest.usedRecipesNames());
        return ResponseEntity.ok(new CreateDayResponse(SUCCESS_MSG_MATCHING_RECIPE, nextDayOfPlan));
    }

    @PostMapping("/days/change")
    public ResponseEntity<CreateDayResponse> changeDay(@RequestBody ChangeDayRequest changeDayRequest){
        PreferencesDto preferencesDto = getPreferences(changeDayRequest.savedPrefers(), changeDayRequest.mealsValues());
        DayResult changedDay = planGeneratorFacade.changeLastDay(preferencesDto, changeDayRequest.recipesNamesToChange());
        return ResponseEntity.ok(new CreateDayResponse(SUCCESS_MSG_MATCHING_RECIPE, changedDay));
    }

    @PostMapping("/days/accept")
    public void acceptDay(@RequestBody AcceptDayRequest acceptDayRequest){
        planGeneratorFacade.processLeftoversAndGroceryList(acceptDayRequest.portions(), acceptDayRequest.tempRecipes());
    }

    private static PreferencesDto getPreferences(SavedPrefers savedPrefers, List<MealValues> mealsValues) {
        return PreferencesDto.builder()
                .savedPrefers(savedPrefers)
                .mealsValues(mealsValues)
                .build();
    }
}
