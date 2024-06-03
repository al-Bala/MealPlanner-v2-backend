package com.mealplannerv2.plangenerator.recipefilter;

import com.mealplannerv2.product.productkeeper.IngredientDtoComparator;
import com.mealplannerv2.product.productkeeper.ProductKeeperFacade;
import com.mealplannerv2.plangenerator.recipefilter.dto.IngredientDto;
import com.mealplannerv2.plangenerator.recipefilter.dto.RecipeDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import static com.mealplannerv2.product.productkeeper.ProductKeeperFacade.productsInUse;
import static org.assertj.core.api.Assertions.assertThat;

class RecipeChooserTest {

    private final RecipeChooser recipeChooser = new RecipeChooser();
    private IngredientDto ing1;
    private IngredientDto ing2;
    private IngredientDto ing3;

    @BeforeEach
    void setUp() {
        IngredientDtoComparator comparator = new IngredientDtoComparator();
        productsInUse = new TreeSet<>(comparator);

        ing1 = new IngredientDto("name1", 1.0, "", LocalDate.of(2024, 5, 19));
        ing2 = new IngredientDto("name2", 1.0, "", LocalDate.of(2024, 5, 18));
        ing3 = new IngredientDto("name3", 1.0, "", LocalDate.of(2024, 5, 18));
        ProductKeeperFacade.addAllUniq(List.of(ing1, ing2, ing3));
    }

    @Test
    void should_return_recipe_with_the_biggest_number_of_matching_ingredients() {
        // given
        List<RecipeDto> testRecipes = new ArrayList<>();
        testRecipes.add(new RecipeDto("recipe1", List.of(ing1, ing2, new IngredientDto("ing0", 0.0, ""))));
        testRecipes.add(new RecipeDto("recipe2", List.of(ing3, new IngredientDto("ing0", 0.0, ""))));
        testRecipes.add(new RecipeDto("recipe3", List.of(ing1, ing2, ing3)));
        //when
        RecipeDto theBestRecipe = recipeChooser.getTheBestRecipe(testRecipes);
        //then
        assertThat(theBestRecipe.getName()).isEqualTo("recipe3");
    }

    @Test
    void should_random_return_one_of_recipes_witch_has_the_same_number_of_matching_ingredients() {
        // given
        List<RecipeDto> testRecipes = new ArrayList<>();
        testRecipes.add(new RecipeDto("recipe1", List.of(ing1, ing2, new IngredientDto("ing0", 0.0, ""))));
        testRecipes.add(new RecipeDto("recipe2", List.of(ing1, ing2, new IngredientDto("ing0", 0.0, ""))));
        testRecipes.add(new RecipeDto("recipe3", List.of(ing1)));
        //when
        RecipeDto theBestRecipe = recipeChooser.getTheBestRecipe(testRecipes);
        //then
        assertThat(theBestRecipe.getName()).containsAnyOf("recipe1", "recipe2");
    }
}