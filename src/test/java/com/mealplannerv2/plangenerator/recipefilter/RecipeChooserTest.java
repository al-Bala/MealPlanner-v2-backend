package com.mealplannerv2.plangenerator.recipefilter;

import com.mealplannerv2.plangenerator.recipefilter.dto.IngredientDto;
import com.mealplannerv2.plangenerator.recipefilter.dto.RecipeDto;
import com.mealplannerv2.productstorage.ProductStorageFacade;
import com.mealplannerv2.productstorage.dto.StoredProductDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecipeChooserTest {

    @Mock
    private ProductStorageFacade productStorageFacade;

    private RecipeChooser recipeChooser;

    private Map<String, StoredProductDto> storedProducts;
    private IngredientDto ing1;
    private IngredientDto ing2;

    @BeforeEach
    void setUp() {
        recipeChooser = new RecipeChooser(productStorageFacade);

        StoredProductDto p1 = new StoredProductDto("name1","");
        StoredProductDto p2 = new StoredProductDto("name2", "");
        storedProducts = new HashMap<>();
        storedProducts.put(p1.getName(), p1);
        storedProducts.put(p2.getName(), p2);

        ing1 = new IngredientDto("name1","");
        ing2 = new IngredientDto("name2","");
    }

    @Test
    void should_return_recipe_with_the_most_matching_ingredients() {
        // given
        RecipeDto recipe1 = new RecipeDto("recipe1", List.of(ing1,ing2));
        RecipeDto recipe2 = new RecipeDto("recipe2", List.of(ing1));
        List<RecipeDto> recipes = Arrays.asList(recipe1,recipe2);
        //when
        when(productStorageFacade.getStoredProducts()).thenReturn(storedProducts);
        RecipeDto theBestRecipe = recipeChooser.getRecipeWithTheMostMatchingOtherIngredients(recipes);
        //then
        assertThat(theBestRecipe.getName()).isEqualTo("recipe1");
    }

    @Test
    void should_random_return_one_of_recipes_witch_has_the_same_number_of_matching_ingredients() {
        // given
        RecipeDto recipe1 = new RecipeDto("recipe1", List.of(ing1));
        RecipeDto recipe2 = new RecipeDto("recipe2", List.of(ing2));
        List<RecipeDto> recipes = Arrays.asList(recipe1,recipe2);
        //when
        when(productStorageFacade.getStoredProducts()).thenReturn(storedProducts);
        RecipeDto theBestRecipe = recipeChooser.getRecipeWithTheMostMatchingOtherIngredients(recipes);
        //then
        assertThat(theBestRecipe.getName()).containsAnyOf("recipe1", "recipe2");
    }
}