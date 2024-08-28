package com.mealplannerv2.plangenerator.infrastructure.controller;

import com.mealplannerv2.plangenerator.DataForRecipeFiltering;
import com.mealplannerv2.plangenerator.PlanGeneratorFacade;
import com.mealplannerv2.plangenerator.infrastructure.controller.dto.Preferences;
import com.mealplannerv2.plangenerator.infrastructure.controller.dto.UnchangingPrefers;
import com.mealplannerv2.plangenerator.infrastructure.controller.dto.request.FirstDayRequest;
import com.mealplannerv2.plangenerator.infrastructure.controller.dto.request.NextDayRequest;
import com.mealplannerv2.plangenerator.recipefilter.RecipeFetcherFacade;
import com.mealplannerv2.plangenerator.recipefilter.dto.RecipeDto;
import com.mealplannerv2.plangenerator.recipefilter.model.Ingredient;
import com.mealplannerv2.recipe.PlannedDayDb;
import com.mealplannerv2.storage.IngredientDto;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/plan")
class PlanGeneratorController {

    private final PlanGeneratorFacade planGeneratorFacade;
    private final RecipeFetcherFacade recipeFetcherFacade;

    @PostMapping("/firstDay")
    public ResponseEntity<PlannedDayDb> firstPlanDay(@RequestBody FirstDayRequest firstDayRequest){
        System.out.println("Object: " + firstDayRequest);
        Preferences preferences = Preferences.builder()
                .unchangingPrefers(firstDayRequest.unchangingPrefers())
                .date(firstDayRequest.date())
                .mealsValues(firstDayRequest.mealsValues())
                .build();
        List<IngredientDto> userIngs = firstDayRequest.userProducts();
        PlannedDayDb firstDayOfPlan = planGeneratorFacade.createFirstDayOfPlan(preferences, userIngs);
        System.out.println("RESULT: " + firstDayOfPlan);
        return ResponseEntity.ok(firstDayOfPlan);
    }

    @PostMapping("/nextDay")
    public ResponseEntity<PlannedDayDb> nextPlanDay(@RequestBody NextDayRequest nextDayRequest){
        UnchangingPrefers unchangingPrefers = new UnchangingPrefers(
                "wegetariańska",
                2,
                List.of("kiwi")
        );
        Preferences preferences = Preferences.builder()
                .unchangingPrefers(unchangingPrefers)
                .date(nextDayRequest.date())
                .mealsValues(nextDayRequest.mealsValues())
                .build();
        PlannedDayDb nextDayOfPlan = planGeneratorFacade.createNextDayOfPlan(preferences);
        return ResponseEntity.ok(nextDayOfPlan);
    }

    @PostMapping("/changeDay")
    public ResponseEntity<PlannedDayDb> changeDay(@RequestBody NextDayRequest nextDayRequest){
        return null;
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

    @GetMapping("/test")
    public String test() {
        return "Correct ";
    }
}
