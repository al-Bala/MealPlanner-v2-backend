package com.mealplannerv2.recipe;

import com.mealplannerv2.InfoForFiltering;
import com.mealplannerv2.entity.Recipe;
import com.mealplannerv2.repository.MealsFilterRepositoryImpl;
import com.mealplannerv2.repository.RecipeDto;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.mealplannerv2.recipe.RecipeMapper.mapFromRecipeListToRecipeDtoList;
import static com.mealplannerv2.recipe.RecipeMapper.mapFromRecipeToRecipeDto;

@AllArgsConstructor
@Log4j2
@Component
public class RecipeFetcherFacade {

    private final MealsFilterRepositoryImpl mealsFilterRepository;
    private final RecipeChooser recipeChooser;

    public RecipeDto fetchRecipeByPreferences(InfoForFiltering info){
        List<Recipe> matchingIngNamesAndAmounts = mealsFilterRepository.findRecipesWithMatchingIngNamesAndAmounts(info);
        if(matchingIngNamesAndAmounts == null){
            log.info("Not found any recipe with matching ingredient's names and amounts.");
            return findOnlyByIngNames(info);
        }
        else if(matchingIngNamesAndAmounts.size() == 1){
            return mapFromRecipeToRecipeDto(matchingIngNamesAndAmounts.get(0));
        }
        else {
            List<RecipeDto> recipesDto = mapFromRecipeListToRecipeDtoList(matchingIngNamesAndAmounts);
            return recipeChooser.getTheBestRecipe(recipesDto);
        }
    }

    private RecipeDto findOnlyByIngNames(InfoForFiltering info){
        List<Recipe> matchingIngNames = mealsFilterRepository.findRecipesWithMatchingIngNames(info);
        if(matchingIngNames == null){
            log.error("Not found any recipe with matching ingredient's names.");
        } else if (matchingIngNames.size() == 1){
            return mapFromRecipeToRecipeDto(matchingIngNames.get(0));
        } else {
            List<RecipeDto> recipesDto = mapFromRecipeListToRecipeDtoList(matchingIngNames);
            return recipeChooser.getTheBestRecipe(recipesDto);
        }
        return null;
    }

}
