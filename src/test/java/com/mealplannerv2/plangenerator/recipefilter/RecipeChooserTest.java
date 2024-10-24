package com.mealplannerv2.plangenerator.recipefilter;

import com.mealplannerv2.plangenerator.recipefilter.dto.RecipeDto;
import com.mealplannerv2.storage.IngredientDto;
import com.mealplannerv2.storage.leftovers.LeftoverDto;
import com.mealplannerv2.storage.leftovers.LeftoversFacade;
import com.mealplannerv2.storage.useringredients.UserIngsFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecipeChooserTest {

    @Mock
    private LeftoversFacade leftoversFacade;
    @Mock
    private UserIngsFacade userIngsFacade;

    private RecipeChooser recipeChooser;

    private Map<String, LeftoverDto> storedProducts;
    private IngredientDto ing0s;
    private IngredientDto ing1s;
    private IngredientDto ing2s;

    private Map<String, IngredientDto> userProduct;
    private IngredientDto ing3u;
    private IngredientDto ing4u;

    @BeforeEach
    void setUp() {
        recipeChooser = new RecipeChooser(leftoversFacade, userIngsFacade);

        LeftoverDto p0 = new LeftoverDto("name0","");
        LeftoverDto p1 = new LeftoverDto("name1","");
        LeftoverDto p2 = new LeftoverDto("name2", "");
        storedProducts = new HashMap<>();
        storedProducts.put(p0.getName(), p0);
        storedProducts.put(p1.getName(), p1);
        storedProducts.put(p2.getName(), p2);

        IngredientDto p3 = new IngredientDto("name3","");
        IngredientDto p4 = new IngredientDto("name4", "");
        userProduct = new HashMap<>();
        userProduct.put(p3.getName(), p3);
//        userProduct.put(p4.getName(), p4);

        ing0s = new IngredientDto("name0","");
        ing1s = new IngredientDto("name1","");
        ing2s = new IngredientDto("name2","");
        ing3u = new IngredientDto("name3","");
        ing4u = new IngredientDto("name4","");
    }

    @Test
    void should_return_recipe_with_matching_user_ing_when_in_both_maps_there_is_one_matching_ing() {
        // given
        RecipeDto recipe1 = new RecipeDto("recipe1", List.of(ing1s));
        RecipeDto recipe2 = new RecipeDto("recipe2", List.of(ing3u));
        List<RecipeDto> recipes = Arrays.asList(recipe1,recipe2);
        //when
        when(leftoversFacade.getLeftovers()).thenReturn(storedProducts);
        when(userIngsFacade.getUserIngs()).thenReturn(userProduct);
        RecipeDto theBestRecipe = recipeChooser.getRecipeWithTheMostMatchingOtherIngredients(recipes);
        //then
        assertThat(theBestRecipe.getName()).isEqualTo("recipe2");
    }

    @Test
    void should_random_return_one_of_recipes_when_there_is_one_matching_user_ing_and_two_stored_products() {
        // given
        RecipeDto recipe1 = new RecipeDto("recipe1", List.of(ing1s, ing2s));
        RecipeDto recipe2 = new RecipeDto("recipe2", List.of(ing3u));
        List<RecipeDto> recipes = Arrays.asList(recipe1,recipe2);
        //when
        when(leftoversFacade.getLeftovers()).thenReturn(storedProducts);
        when(userIngsFacade.getUserIngs()).thenReturn(userProduct);
        RecipeDto theBestRecipe = recipeChooser.getRecipeWithTheMostMatchingOtherIngredients(recipes);
        //then
        assertThat(theBestRecipe.getName()).containsAnyOf("recipe1", "recipe2");
        System.out.println(theBestRecipe.getName());
    }

    @Test
    void should_return_recipe_with_matching_stored_product_when_there_is_one_matching_user_ing_and_three_stored_products() {
        // given
        RecipeDto recipe1 = new RecipeDto("recipe1", List.of(ing0s, ing1s, ing2s));
        RecipeDto recipe2 = new RecipeDto("recipe2", List.of(ing3u));
        List<RecipeDto> recipes = Arrays.asList(recipe1,recipe2);
        //when
        when(leftoversFacade.getLeftovers()).thenReturn(storedProducts);
        when(userIngsFacade.getUserIngs()).thenReturn(userProduct);
        RecipeDto theBestRecipe = recipeChooser.getRecipeWithTheMostMatchingOtherIngredients(recipes);
        //then
        assertThat(theBestRecipe.getName()).isEqualTo("recipe1");
    }
}