package com.mealplannerv2;

import com.mealplannerv2.entity.Recipe;
import com.mealplannerv2.repository.IngredientDto;
import com.mealplannerv2.repository.MealsFilterRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@AllArgsConstructor
@Controller
@RequestMapping("/plan")
class PlanGeneratorController {

    private final MealsFilterRepository repository;

    @GetMapping("")
    public String find() {
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

        List<Recipe> matchingRecipes = repository.findRecipesWithMatchingIngNamesAndAmounts(info);
        System.out.println(matchingRecipes);
        return matchingRecipes.toString();
    }

}
