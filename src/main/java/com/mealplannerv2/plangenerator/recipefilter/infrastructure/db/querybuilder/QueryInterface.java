package com.mealplannerv2.plangenerator.recipefilter.infrastructure.db.querybuilder;

import com.mealplannerv2.recipe.model.Ingredient;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.List;

public interface QueryInterface {
    void setRecipesFromHistory(List<String> recipesFromHistory);
    void setChangedRecipes(List<String> usedRecipes);
    void setTypeOfMeal(String typeOfMeal);
    void setDietId(String diet);
    void setMaxStorageTime(int daysNr);
    void setPrepareTime(Integer time);
    void setIngredientsToUseFirstly(List<Ingredient> ingredients, Criteria namesAmountCriteria);
    void setProductsToAvoid(List<String> productsToAvoid);

    Aggregation getAggregation();
    void clearOperationsList();

}
