package com.mealplannerv2.plangenerator;

import com.mealplannerv2.InfoFromUser;
import com.mealplannerv2.plangenerator.recipefilter.RecipeFetcherFacade;
import com.mealplannerv2.plangenerator.recipefilter.dto.RecipeDto;
import com.mealplannerv2.plangenerator.recipefilter.model.Ingredient;
import com.mealplannerv2.productstorage.dto.StoredProductDto;
import com.mealplannerv2.productstorage.ProductStorageFacade;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@AllArgsConstructor
@Component
public class PlanGeneratorFacade {

    private final RecipeFetcherFacade recipeFetcherFacade;
    private final ProductStorageFacade productStorageFacade;

    public PlannedDay createFirstDayOfPlan(InfoFromUser info){

        // pierszy dzień kiedy productsInUse jest puste
        List<StoredProductDto> newProductsToStore = productStorageFacade.createNewProducts(info.userProducts());
        productStorageFacade.addAllToStoredProducts(newProductsToStore);

        // wybranie produktów z 1 grupy
        List<StoredProductDto> productsWhichMustBeUsedFirstly = productStorageFacade.getProductsWhichMustBeUsedFirstly();

        List<Ingredient> ingredients = PlanGeneratorMapper.mapFromStoredProductsToIngredients(productsWhichMustBeUsedFirstly);
        DataForRecipeFiltering dataForRecipesFiltering = DataForRecipeFiltering.builder()
                .forHowManyDays(info.forHowManyDays())
                .diet(info.diet())
                .timeForPrepareMin(info.timeForPrepareMin())
                .ingredientsToUseFirstly(ingredients)
                .productsToAvoid(info.productsToAvoid())
                .build();

        RecipeDto recipeDto = recipeFetcherFacade.fetchRecipeByPreferences(dataForRecipesFiltering);

        // na koniec po obliczeniu resztek i dodaniu ich do storage
        productStorageFacade.updateSpoilDatesForStoredProducts();
        return null;
    }

    public PlannedDay createNextDayOfPlan(){
        return null;
    }

    public PlannedDay changeLastDay(PlannedDay dayToChange){
        return null;
    }

}
