package com.mealplannerv2.plangenerator;

import com.mealplannerv2.plangenerator.grocerylist.GroceryListFacade;
import com.mealplannerv2.plangenerator.infrastructure.controller.dto.DayInfoDto;
import com.mealplannerv2.plangenerator.infrastructure.controller.dto.PreferencesInfo;
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
    private final MealService mealService;
    private final PlanGeneratorMapper mapper;
    private final IngredientsCalculator ingredientsCalculator;
    private final ProductFacade productFacade;
    private final GroceryListFacade groceryListFacade;

    public PlannedDay createFirstDayOfPlan(PreferencesInfo preferences, DayInfoDto dayInfoDto){
//        if(dayInfo.meals().isEmpty()){
//            log.info("Skip day");
//            return null;
//        }
        // pierszy dzień kiedy productsInUse jest puste
        List<IngredientDto> userIngs = mapper.mapFromProductsFromUserToIngredientDto(preferences.userProducts());
        addToProductStorage(userIngs);

        // wybranie produktów z 1 grupy
        List<StoredProductDto> productsWhichMustBeUsedFirstly = productStorageFacade.getProductsWhichMustBeUsedFirstly();
        List<Ingredient> ingredients = PlanGeneratorMapper.mapFromStoredProductsToIngredients(productsWhichMustBeUsedFirstly);

        List<Meal> sortedMeals = mealService.sortMealsByPriority(dayInfoDto.meals());

        List<RecipeForDay> recipesForDay = new ArrayList<>();
        for(Meal meal: sortedMeals){
            DataForRecipeFiltering dataForRecipesFiltering = DataForRecipeFiltering.builder()
                    .diet(preferences.diet())
                    .typeOfMeal(meal.getName())
                    .forHowManyDays(mealService.getForHowManyDays(meal))
                    .timeForPrepareMin(meal.getTimeMin())
                    .ingredientsToUseFirstly(ingredients)
                    .productsToAvoid(preferences.productsToAvoid())
                    .build();

            RecipeDto matchingRecipe = recipeFetcherFacade.fetchRecipeByPreferences(dataForRecipesFiltering);
            ingredientsCalculator.setCalculatedIngredients(matchingRecipe, preferences.portions(), dataForRecipesFiltering.forHowManyDays());
            recipesForDay.add(new RecipeForDay(meal.getName(), matchingRecipe));

            List<IngredientDto> ingsFromRecipe = matchingRecipe.getIngredients();
            // update: odjęcie posiadanych już produktów
            List<IngredientDto> updatedIngs = productStorageFacade.updateStoredProductsAndIngs(ingsFromRecipe);
            groceryListFacade.addAllToGroceryList(updatedIngs);

            List<ChosenPacket> allNeededPackets = productFacade.choosePacketForEachIngredient(updatedIngs);
            List<IngredientDto> leftovers = allNeededPackets.stream()
                    .map(packet -> new IngredientDto(
                            packet.getIngredientDto().getName(),
                            packet.getLeftovers(),
                            packet.getIngredientDto().getUnit()))
                    .toList();
            addToProductStorage(leftovers);
        }
        // na koniec po obliczeniu resztek i dodaniu ich do storage
        productStorageFacade.updateSpoilDatesForStoredProducts();
        return PlannedDay.builder()
                .date("date")
                .recipesForDay(recipesForDay)
                .build();
    }

    private void addToProductStorage(List<IngredientDto> ingredients) {
        List<StoredProductDto> convertedProductsToStore = productStorageFacade.convertIntoStoredProducts(ingredients);
        productStorageFacade.addAllToStoredProducts(convertedProductsToStore);
    }

    public PlannedDay createNextDayOfPlan(){
        return null;
    }

    public PlannedDay changeLastDay(PlannedDay dayToChange){
        return null;
    }

}
