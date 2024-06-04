package com.mealplannerv2.plangenerator.infrastructure.controller;

import com.mealplannerv2.InfoForFiltering;
import com.mealplannerv2.plangenerator.recipefilter.RecipeFetcherFacade;
import com.mealplannerv2.plangenerator.recipefilter.dto.IngredientDto;
import com.mealplannerv2.plangenerator.recipefilter.dto.RecipeDto;
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
        InfoForFiltering info = InfoForFiltering.builder()
                .forHowManyDays(1)
//                .diet("wegetariańska")
//                .timeForPrepareMin(5)
//                .userProducts(List.of("marchew"))
//                .productsToAvoid(List.of("oliwki"))
                .userProducts(List.of(
                        new IngredientDto("truskawki", 100.0, ""),
                        new IngredientDto("marchew", 2.0, ""),
                        new IngredientDto("ryż", 300.0, "")))
                .build();

        RecipeDto result = recipeFetcherFacade.fetchRecipeByPreferences(info);
        System.out.println(result);
        return ResponseEntity.ok(new PlanResponseDto(result));
    }

}
