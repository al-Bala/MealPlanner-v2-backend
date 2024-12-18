package com.mealplannerv2.plangenerator.recipefilter;

import com.mealplannerv2.plangenerator.RecipeFilters;
import com.mealplannerv2.plangenerator.recipefilter.error.NotFoundMatchingRecipeException;
import com.mealplannerv2.recipe.model.Recipe;
import com.mealplannerv2.plangenerator.recipefilter.infrastructure.db.RecipeFilterRepositoryImpl;
import com.mealplannerv2.plangenerator.recipefilter.dto.RecipeDto;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;


@AllArgsConstructor
@Log4j2
@Component
public class RecipeFetcherFacade {

    private final RecipeFilterRepositoryImpl mealsFilterRepository;
    private final RecipeChooser recipeChooser;

    public RecipeDto fetchRecipeByPreferences(RecipeFilters info2){

        List<Recipe> matchingIngNamesAndAmounts = mealsFilterRepository.findRecipesWithMatchingIngNamesAndAmounts(info2);
        if(matchingIngNamesAndAmounts == null){
            log.info("[First try] Not found any recipe with matching ingredient's names and amounts.");
            return findOnlyByIngNames(info2);
        }
        else if(matchingIngNamesAndAmounts.size() == 1){
            return RecipeMapper.mapFromRecipeToRecipeDto(matchingIngNamesAndAmounts.get(0));
        }
        else {
            List<RecipeDto> recipesDto = RecipeMapper.mapFromRecipeListToRecipeDtoList(matchingIngNamesAndAmounts);
            return recipeChooser.getRecipeWithTheMostMatchingOtherIngredients(recipesDto);
        }
    }

    private RecipeDto findOnlyByIngNames(RecipeFilters info){
        List<Recipe> matchingIngNames = mealsFilterRepository.findRecipesWithMatchingIngNames(info);
        if(matchingIngNames == null){
            log.error("[Secound try] Not found any recipe with matching ingredient's name.");
//            return null;
            throw new NotFoundMatchingRecipeException("Not found any matching recipe.");
        } else if (matchingIngNames.size() == 1){
            return RecipeMapper.mapFromRecipeToRecipeDto(matchingIngNames.get(0));
        } else {
            List<RecipeDto> recipesDto = RecipeMapper.mapFromRecipeListToRecipeDtoList(matchingIngNames);
            return recipeChooser.getRecipeWithTheMostMatchingOtherIngredients(recipesDto);
        }

    }

}
