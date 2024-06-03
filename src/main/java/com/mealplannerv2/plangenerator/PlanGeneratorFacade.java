package com.mealplannerv2.plangenerator;

import com.mealplannerv2.InfoForFiltering;
import com.mealplannerv2.plangenerator.recipefilter.RecipeFetcherFacade;
import com.mealplannerv2.plangenerator.recipefilter.dto.RecipeDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class PlanGeneratorFacade {

    private final RecipeFetcherFacade recipeFetcherFacade;

    public PlannedDay createPlan(InfoForFiltering info){

        // aktualizacja productsInUse
        // wybranie produktów z 1 grupy

        

        RecipeDto recipeDto = recipeFetcherFacade.fetchRecipeByPreferences(info);
        return null;
    }

}
