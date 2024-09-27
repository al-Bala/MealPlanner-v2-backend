package com.mealplannerv2.plangenerator.recipefilter.infrastructure.db.querybuilder;

import com.mealplannerv2.ChangedRecipesList;
import com.mealplannerv2.plangenerator.RecipeFilters;
import com.mealplannerv2.user.UserFacade;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Component
public class Director {

    private final QueryMaker queryMaker;
    private final NACriteriaBuilder namesAmountsCriteria = new NamesAmountsCriteria();
    private final NACriteriaBuilder onlyNamesCriteria = new OnlyNamesCriteria();
    private final List<Aggregation> aggregations = new ArrayList<>();
    private final UserFacade userFacade;

    public List<Aggregation> namesAndAmounts(RecipeFilters info){
        aggregations.clear();
        aggregations.add(firstTry(info, namesAmountsCriteria));
        aggregations.add(secondTry(info, namesAmountsCriteria));
        aggregations.add(thirdTry(info, namesAmountsCriteria));
//        aggregations.add(fourthTry(info));
        return aggregations;
    }

    public List<Aggregation> names(RecipeFilters info){
        aggregations.clear();
        aggregations.add(firstTry(info, onlyNamesCriteria));
        aggregations.add(secondTry(info, onlyNamesCriteria));
        aggregations.add(thirdTry(info, onlyNamesCriteria));
        aggregations.add(fourthTry(info));
        return aggregations;
    }

    Aggregation fourthTry(RecipeFilters info){
        queryMaker.clearOperationsList();
        queryMaker.setTypeOfMeal(info.typeOfMeal());
        queryMaker.setMaxStorageTime(info.forHowManyDays());
        queryMaker.setDiet(info.diet());
        queryMaker.setChangedRecipes(ChangedRecipesList.usedRecipes);
        queryMaker.setRecipesFromHistory(userFacade.getRecipesNames());
        return queryMaker.getAggregation();
    }

    Aggregation thirdTry(RecipeFilters info, NACriteriaBuilder criteria){
        fourthTry(info);
        queryMaker.setIngredientsToUseFirstly(info.ingredientsToUseFirstly(), criteria.getCriteria(info.ingredientsToUseFirstly()));
        return queryMaker.getAggregation();
    }

    Aggregation secondTry(RecipeFilters info, NACriteriaBuilder criteria){
        thirdTry(info, criteria);
        queryMaker.setPrepareTime(info.timeForPrepareMin());
        return queryMaker.getAggregation();
    }

    Aggregation firstTry(RecipeFilters info, NACriteriaBuilder criteria){
        secondTry(info, criteria);
        queryMaker.setProductsToAvoid(info.productsToAvoid());
        return queryMaker.getAggregation();
    }
}
