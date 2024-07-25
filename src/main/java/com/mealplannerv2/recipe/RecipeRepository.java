package com.mealplannerv2.recipe;

import com.mealplannerv2.plangenerator.recipefilter.model.Recipe;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RecipeRepository extends MongoRepository<Recipe, String> {
    Optional<Recipe> findById(ObjectId id);
}
