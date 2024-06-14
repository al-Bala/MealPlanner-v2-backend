package com.mealplannerv2.plangenerator;

import com.mealplannerv2.plangenerator.grocerylist.GroceryListFacade;
import com.mealplannerv2.plangenerator.infrastructure.controller.dto.*;
import com.mealplannerv2.plangenerator.infrastructure.controller.dto.meal.Meal;
import com.mealplannerv2.plangenerator.recipefilter.RecipeFetcherFacade;
import com.mealplannerv2.plangenerator.recipefilter.dto.IngredientDto;
import com.mealplannerv2.plangenerator.recipefilter.dto.RecipeDto;
import com.mealplannerv2.plangenerator.recipefilter.model.Ingredient;
import com.mealplannerv2.product.ProductFacade;
import com.mealplannerv2.product.dto.ChosenPacket;
import com.mealplannerv2.productstorage.ProductStorageFacade;
import com.mealplannerv2.productstorage.dto.StoredProductDto;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@AllArgsConstructor
@Component
public class PlanGeneratorFacade {

    private final RecipeFetcherFacade recipeFetcherFacade;
    private final ProductStorageFacade productStorageFacade;
    private final PlanGeneratorMapper mapper;
    private final IngredientsCalculator ingredientsCalculator;
    private final ProductFacade productFacade;
    private final GroceryListFacade groceryListFacade;

    public PlannedDay createFirstDayOfPlan(Preferences preferences, List<ProductFromUser> productsFromUser){
        addProductsFromUserToStoredProducts(productsFromUser);
        return createDay(preferences);
    }

    public PlannedDay createNextDayOfPlan(Preferences preferences){
        if (isAnyMealSelected(preferences.mealsValues())){
            return null;
        }
        return createDay(preferences);
    }

    public PlannedDay createDay(Preferences preferences){
        UnchangingPrefers unchangingPrefers = preferences.unchangingPrefers();
        List<MealValues> mealValues = preferences.mealsValues();
        List<RecipeForDay> recipesForDay = new ArrayList<>();

        List<Meal> meals = Meal.getAllMeals(mealValues);
        for(Meal meal: meals){
            // wybranie produktów z 1 grupy
            List<StoredProductDto> productsWhichMustBeUsedFirstly = productStorageFacade.getProductsWhichMustBeUsedFirstly();
            List<Ingredient> ingredients = PlanGeneratorMapper.mapFromStoredProductsToIngredients(productsWhichMustBeUsedFirstly);

            DataForRecipeFiltering dataForRecipesFiltering = DataForRecipeFiltering.builder()
                    .diet(unchangingPrefers.diet())
                    .typeOfMeal(meal.getName())
                    .forHowManyDays(meal.getForHowManyDays())
                    .timeForPrepareMin(meal.getTimeMin())
                    .ingredientsToUseFirstly(ingredients)
                    .productsToAvoid(unchangingPrefers.productsToAvoid())
                    .build();

            RecipeDto matchingRecipe = recipeFetcherFacade.fetchRecipeByPreferences(dataForRecipesFiltering);
            ingredientsCalculator.setCalculatedIngredients(matchingRecipe, unchangingPrefers.portions(), dataForRecipesFiltering.forHowManyDays());
            recipesForDay.add(new RecipeForDay(meal.getName(), matchingRecipe));

            List<IngredientDto> ingsInRecipe = matchingRecipe.getIngredients();
            // update: odjęcie posiadanych już produktów
            List<IngredientDto> updatedIngs = productStorageFacade.subtractStoredProductsFromIngsInRecipe(ingsInRecipe);
            groceryListFacade.addAllToGroceryList(updatedIngs);

            choosePacketsAndSaveLeftovers(updatedIngs);
        }
        // na koniec po obliczeniu resztek i dodaniu ich do storage
        productStorageFacade.updateSpoilDatesForStoredProducts();
        return PlannedDay.builder()
                .date(preferences.date())
                .recipesForDay(recipesForDay)
                .build();
    }

    private void choosePacketsAndSaveLeftovers(List<IngredientDto> updatedIngs) {
        List<ChosenPacket> allNeededPackets = productFacade.choosePacketForEachIngredient(updatedIngs);
        List<IngredientDto> leftovers = allNeededPackets.stream()
                .map(packet -> new IngredientDto(
                        packet.getIngredientDto().getName(),
                        packet.getLeftovers(),
                        packet.getIngredientDto().getUnit()))
                .filter(ing -> ing.getAmount() > 0)
                .toList();
        productStorageFacade.convertAndAddToStoredProducts(leftovers);
    }

    private void addProductsFromUserToStoredProducts(List<ProductFromUser> productsFromUsers) {
        List<IngredientDto> userIngs = mapper.mapFromProductsFromUserToIngredientDto(productsFromUsers);
        productStorageFacade.convertAndAddToStoredProducts(userIngs);
    }

    private static boolean isAnyMealSelected(List<MealValues> mealsValues) {
        if(mealsValues.isEmpty()){
            log.info("Skip day");
            return true;
        }
        return false;
    }

    public PlannedDay changeLastDay(PlannedDay dayToChange){
        return null;
    }

}
