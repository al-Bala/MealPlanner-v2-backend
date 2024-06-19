package com.mealplannerv2.plangenerator;

import com.mealplannerv2.grocerylist.GroceryListFacade;
import com.mealplannerv2.plangenerator.infrastructure.controller.dto.*;
import com.mealplannerv2.plangenerator.infrastructure.controller.dto.meal.Meal;
import com.mealplannerv2.plangenerator.recipefilter.RecipeFetcherFacade;
import com.mealplannerv2.plangenerator.recipefilter.dto.IngredientDto;
import com.mealplannerv2.plangenerator.recipefilter.dto.RecipeDto;
import com.mealplannerv2.plangenerator.recipefilter.model.Ingredient;
import com.mealplannerv2.product.ProductFacade;
import com.mealplannerv2.product.dto.ChosenPacket;
import com.mealplannerv2.productstorage.storedleftovers.StoredLeftoverDto;
import com.mealplannerv2.productstorage.storedleftovers.StoredLeftoversFacade;
import com.mealplannerv2.productstorage.userproducts.UserProductDto;
import com.mealplannerv2.productstorage.userproducts.UserProductsFacade;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.*;

@Log4j2
@AllArgsConstructor
@Component
public class PlanGeneratorFacade {

    private final RecipeFetcherFacade recipeFetcherFacade;
    private final StoredLeftoversFacade storedLeftoversFacade;
    private final UserProductsFacade userProductsFacade;
    private final PlanGeneratorMapper mapper;
    private final IngredientsCalculator ingredientsCalculator;
    private final ProductFacade productFacade;
    private final GroceryListFacade groceryListFacade;

    public PlannedDay createFirstDayOfPlan(Preferences preferences, List<ProductFromUser> productsFromUser){
        addToUserProducts(productsFromUser);
        return createDay(preferences);
    }

    public PlannedDay createNextDayOfPlan(Preferences preferences){
        if (isNoMealSelected(preferences.mealsValues())){
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

            List<Ingredient> ingredients;
            if(!userProductsFacade.getUserProducts().isEmpty()){
                Collection<UserProductDto> values = userProductsFacade.getUserProducts().values();
                ingredients = PlanGeneratorMapper.mapFromUserProductsToIngredients(values);
            } else {
                // wybranie produktów z 1 grupy
                List<StoredLeftoverDto> productsWhichMustBeUsedFirstly = storedLeftoversFacade.getProductsWhichMustBeUsedFirstly();
                ingredients = PlanGeneratorMapper.mapFromStoredProductsToIngredients(productsWhichMustBeUsedFirstly);
            }

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
            List<IngredientDto> ingsToBuy = userProductsFacade.subtractUserProductsFromIngsInRecipe(ingsInRecipe);
            storedLeftoversFacade.updateStoredLeftovers(ingsToBuy);
            groceryListFacade.addAllToGroceryList(ingsToBuy);

            choosePacketsAndSaveLeftovers(ingsToBuy);
        }
        // na koniec po obliczeniu resztek i dodaniu ich do storage
        storedLeftoversFacade.updateSpoilDatesForStoredProducts();
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
        storedLeftoversFacade.convertAndAddToStoredProducts(leftovers);
    }

    private void addToUserProducts(List<ProductFromUser> productsFromUsers) {
        List<IngredientDto> userIngs = mapper.mapFromProductsFromUserToIngredientDto(productsFromUsers);
        userProductsFacade.convertAndAddToUserProducts(userIngs);
    }

    private static boolean isNoMealSelected(List<MealValues> mealsValues) {
        if(mealsValues.isEmpty()){
            log.info("Skip day");
            return true;
        }
        return false;
    }

    public PlannedDay changeLastDay(PlannedDay dayToChange){
        return null;
    }

    public Set<IngredientDto> getGroceryList(){
        return groceryListFacade.getGroceryList();
    }

}
