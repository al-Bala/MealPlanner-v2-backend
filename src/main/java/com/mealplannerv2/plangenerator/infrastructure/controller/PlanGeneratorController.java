package com.mealplannerv2.plangenerator.infrastructure.controller;

import com.mealplannerv2.plangenerator.PlanGeneratorFacade;
import com.mealplannerv2.plangenerator.infrastructure.controller.dto.MealValues;
import com.mealplannerv2.plangenerator.infrastructure.controller.dto.Preferences;
import com.mealplannerv2.plangenerator.infrastructure.controller.dto.SavedPrefers;
import com.mealplannerv2.plangenerator.infrastructure.controller.dto.request.AcceptDayRequest;
import com.mealplannerv2.plangenerator.infrastructure.controller.dto.request.ChangeDayRequest;
import com.mealplannerv2.plangenerator.infrastructure.controller.dto.request.FirstDayRequest;
import com.mealplannerv2.plangenerator.infrastructure.controller.dto.request.NextDayRequest;
import com.mealplannerv2.recipe.DayPlan;
import com.mealplannerv2.storage.IngredientDto;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/generator")
class PlanGeneratorController {
    private final PlanGeneratorFacade planGeneratorFacade;

    @PostMapping("/days/first")
    public ResponseEntity<DayPlan> createFirstDayOfPlan(@RequestBody FirstDayRequest firstDayRequest) {
        Preferences preferences = getPreferences(firstDayRequest.savedPrefers(), firstDayRequest.mealsValues());
        List<IngredientDto> userIngs = firstDayRequest.userProducts();
        DayPlan firstDayOfPlan = planGeneratorFacade.createFirstDayOfPlan(preferences, userIngs);
        System.out.println("RESULT: " + firstDayOfPlan);
        return ResponseEntity.ok(firstDayOfPlan);
    }

    @PostMapping("/days/next")
    public ResponseEntity<DayPlan> createNextDayOfPlan(@RequestBody NextDayRequest nextDayRequest){
        Preferences preferences = getPreferences(nextDayRequest.savedPrefers(), nextDayRequest.mealsValues());
        DayPlan nextDayOfPlan = planGeneratorFacade.createNextDayOfPlan(preferences, nextDayRequest.tempDays());
        return ResponseEntity.ok(nextDayOfPlan);
    }

    @PostMapping("/days/change")
    public ResponseEntity<DayPlan> changeDay(@RequestBody ChangeDayRequest changeDayRequest){
        Preferences preferences = getPreferences(changeDayRequest.savedPrefers(), changeDayRequest.mealsValues());
        DayPlan changedDay = planGeneratorFacade.changeLastDay(preferences, changeDayRequest.tempDay());
        return ResponseEntity.ok(changedDay);
    }

    @PostMapping("/days/accept")
    public void acceptDay(@RequestBody AcceptDayRequest acceptDayRequest){
        planGeneratorFacade.processLeftoversAndGroceryList(acceptDayRequest.portions(), acceptDayRequest.tempDay());
    }

    private static Preferences getPreferences(SavedPrefers savedPrefers, List<MealValues> mealsValues) {
        return Preferences.builder()
                .savedPrefers(savedPrefers)
                .mealsValues(mealsValues)
                .build();
    }
}
