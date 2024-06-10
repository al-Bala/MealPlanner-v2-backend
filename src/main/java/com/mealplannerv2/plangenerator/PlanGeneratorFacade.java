package com.mealplannerv2.plangenerator;

import com.mealplannerv2.plangenerator.infrastructure.controller.dto.DayInfo;
import com.mealplannerv2.plangenerator.infrastructure.controller.dto.PreferencesInfo;
import com.mealplannerv2.plangenerator.infrastructure.controller.dto.meal.Meal;
import com.mealplannerv2.plangenerator.recipefilter.RecipeFetcherFacade;
import com.mealplannerv2.plangenerator.recipefilter.dto.IngredientDto;
import com.mealplannerv2.plangenerator.recipefilter.dto.RecipeDto;
import com.mealplannerv2.plangenerator.recipefilter.model.Ingredient;
import com.mealplannerv2.product.ProductFacade;
import com.mealplannerv2.product.dto.Result;
import com.mealplannerv2.productstorage.ProductStorageFacade;
import com.mealplannerv2.productstorage.dto.StoredProductDto;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

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

    public List<PlannedDay> createFirstDayOfPlan(PreferencesInfo preferences, DayInfo dayInfo){
//        if(dayInfo.meals().isEmpty()){
//            log.info("Skip day");
//            return null;
//        }

        List<IngredientDto> userProducts = mapper.mapFromProductsFromUserToIngredientDto(preferences.userProducts());
        // pierszy dzień kiedy productsInUse jest puste
        addToProductStorage(userProducts);

        // wybranie produktów z 1 grupy
        List<StoredProductDto> productsWhichMustBeUsedFirstly = productStorageFacade.getProductsWhichMustBeUsedFirstly();
        List<Ingredient> ingredients = PlanGeneratorMapper.mapFromStoredProductsToIngredients(productsWhichMustBeUsedFirstly);

        List<Meal> sortedMeals = mealService.sortMealsByPriority(dayInfo.meals());

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
            List<Result> allNeededPackets = productFacade.choosePacketForEachIngredient(matchingRecipe);

            List<IngredientDto> leftovers = allNeededPackets.stream()
                    .map(packet -> new IngredientDto(packet.getIngredientDto().getName(), packet.getLeftovers(), packet.getIngredientDto().getUnit()))
                    .toList();
            addToProductStorage(leftovers);
            // na koniec po obliczeniu resztek i dodaniu ich do storage
            productStorageFacade.updateSpoilDatesForStoredProducts();
        }

        return null;
    }

    private void addToProductStorage(List<IngredientDto> ingredients) {
        List<StoredProductDto> newProductsToStore = productStorageFacade.convertIntoStoredProducts(ingredients);
        productStorageFacade.addAllToStoredProducts(newProductsToStore);
    }

    public PlannedDay createNextDayOfPlan(){
        return null;
    }

    public PlannedDay changeLastDay(PlannedDay dayToChange){
        return null;
    }

}
