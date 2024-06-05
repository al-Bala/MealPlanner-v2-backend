package com.mealplannerv2.plangenerator.infrastructure.controller;

import com.mealplannerv2.plangenerator.DataForRecipeFiltering;
import com.mealplannerv2.plangenerator.recipefilter.RecipeFetcherFacade;
import com.mealplannerv2.plangenerator.recipefilter.dto.RecipeDto;
import com.mealplannerv2.plangenerator.recipefilter.model.Ingredient;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@AllArgsConstructor
@Controller
@RequestMapping("/plan")
class PlanGeneratorController {

    private final RecipeFetcherFacade recipeFetcherFacade;

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
