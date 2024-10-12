package com.mealplannerv2.plangenerator.recipefilter.infrastructure.db.querybuilder;

import com.mealplannerv2.recipe.model.Ingredient;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.List;

public interface NACriteriaBuilder {
    Criteria getCriteria(List<Ingredient> ingredients);
}
