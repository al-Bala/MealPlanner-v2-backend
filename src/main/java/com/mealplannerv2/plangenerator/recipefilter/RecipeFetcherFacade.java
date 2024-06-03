package com.mealplannerv2.plangenerator.recipefilter;

import com.mealplannerv2.plangenerator.InfoFiltering2;
import com.mealplannerv2.InfoForFiltering;
import com.mealplannerv2.plangenerator.recipefilter.model.Ingredient;
import com.mealplannerv2.plangenerator.recipefilter.model.Recipe;
import com.mealplannerv2.product.productkeeper.ProductKeeperFacade;
import com.mealplannerv2.plangenerator.recipefilter.dto.IngredientDto;
import com.mealplannerv2.plangenerator.recipefilter.infrastructure.db.RecipeFilterRepositoryImpl;
import com.mealplannerv2.plangenerator.recipefilter.dto.RecipeDto;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.mealplannerv2.product.productkeeper.ProductKeeperFacade.getAllWithGivenDaysNumber;

@AllArgsConstructor
@Log4j2
@Component
public class RecipeFetcherFacade {

    private final RecipeFilterRepositoryImpl mealsFilterRepository;
    private final RecipeChooser recipeChooser;

    public RecipeDto fetchRecipeByPreferences(InfoForFiltering info){

        InfoFiltering2 info2 = cos(info);

        List<Recipe> matchingIngNamesAndAmounts = mealsFilterRepository.findRecipesWithMatchingIngNamesAndAmounts(info2);
        if(matchingIngNamesAndAmounts == null){
            log.info("Not found any recipe with matching ingredient's names and amounts.");
            return findOnlyByIngNames(info2);
        }
        else if(matchingIngNamesAndAmounts.size() == 1){
            return RecipeMapper.mapFromRecipeToRecipeDto(matchingIngNamesAndAmounts.get(0));
        }
        else {
            List<RecipeDto> recipesDto = RecipeMapper.mapFromRecipeListToRecipeDtoList(matchingIngNamesAndAmounts);
            return recipeChooser.getTheBestRecipe(recipesDto);
        }
    }

    private InfoFiltering2 cos(InfoForFiltering info){
        ProductKeeperFacade.addAllUniq(info.userProducts());
        List<IngredientDto> prioritisedProducts = getAllWithGivenDaysNumber(1);
        List<Ingredient> ingredients = RecipeMapper.mapFromIngredientDroToIngredient(prioritisedProducts);

        return InfoFiltering2.builder()
                .forHowManyDays(info.forHowManyDays())
                .diet(info.diet())
                .timeForPrepareMin(info.timeForPrepareMin())
                .userProducts(ingredients)
                .productsToAvoid(info.productsToAvoid())
                .build();
    }

    private RecipeDto findOnlyByIngNames(InfoFiltering2 info){
        List<Recipe> matchingIngNames = mealsFilterRepository.findRecipesWithMatchingIngNames(info);
        if(matchingIngNames == null){
            log.error("Not found any recipe with matching ingredient's names.");
        } else if (matchingIngNames.size() == 1){
            return RecipeMapper.mapFromRecipeToRecipeDto(matchingIngNames.get(0));
        } else {
            List<RecipeDto> recipesDto = RecipeMapper.mapFromRecipeListToRecipeDtoList(matchingIngNames);
            return recipeChooser.getTheBestRecipe(recipesDto);
        }
        return null;
    }

}
