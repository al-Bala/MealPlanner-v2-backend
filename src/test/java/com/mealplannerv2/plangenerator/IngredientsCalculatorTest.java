package com.mealplannerv2.plangenerator;

import com.mealplannerv2.plangenerator.recipefilter.dto.RecipeDto;
import com.mealplannerv2.storage.IngredientDto;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class IngredientsCalculatorTest {

    @Test
    void should_convert_all_ingredients_from_2_to_4_portions() {
        // given
        IngredientsCalculator ingredientsCalculator = new IngredientsCalculator();
        RecipeDto recipeDto = RecipeDto.builder()
                .portions(2)
                .ingredients(List.of(
                        new IngredientDto("ing1", 100.0, ""),
                        new IngredientDto("ing2", 50.0, "")
                ))
                .build();
        // when
        ingredientsCalculator.setCalculatedIngredients(recipeDto, 4, 1);
        // then
        assertThat(recipeDto.getIngredients()).isEqualTo(List.of(
                new IngredientDto("ing1", 200.0, ""),
                new IngredientDto("ing2", 100.0, "")));
        assertThat(recipeDto.getPortions()).isEqualTo(4);
    }
}