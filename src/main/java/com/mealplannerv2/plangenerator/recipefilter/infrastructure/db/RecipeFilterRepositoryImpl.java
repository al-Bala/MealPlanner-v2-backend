package com.mealplannerv2.plangenerator.recipefilter.infrastructure.db;

import com.mealplannerv2.plangenerator.DataForRecipeFiltering;
import com.mealplannerv2.plangenerator.recipefilter.RecipeFilterRepository;
import com.mealplannerv2.plangenerator.recipefilter.infrastructure.db.querybuilder.Director;
import com.mealplannerv2.plangenerator.recipefilter.model.Recipe;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.stereotype.Repository;

import java.util.List;


@AllArgsConstructor
@Log4j2
@Repository
public class RecipeFilterRepositoryImpl implements RecipeFilterRepository {
    private final MongoTemplate mongoTemplate;
    private final Director director;

    @Override
    public List<Recipe> findRecipesWithMatchingIngNamesAndAmounts(DataForRecipeFiltering info) {
        List<Aggregation> aggregations = director.namesAndAmounts(info);
        return fetchRecipes(aggregations);
    }

    @Override
    public List<Recipe> findRecipesWithMatchingIngNames(DataForRecipeFiltering info) {
        List<Aggregation> aggregations = director.names(info);
        return fetchRecipes(aggregations);
    }

    private List<Recipe> fetchRecipes(List<Aggregation> aggregations) {
        List<Recipe> documents = null;
        for(Aggregation agr: aggregations){
            List<Recipe> currentDocuments  = makeAggregation(agr);
            if(!currentDocuments.isEmpty()){
                documents = currentDocuments;
                break;
            }
        }
        if(documents == null){
            log.error("Nie udało się znaleźć żadnego pasującego przepisu :(");
        }
        return documents;
    }

    private List<Recipe> makeAggregation(Aggregation agr) {
//        System.out.println("AGR:\n" + agr);
        AggregationResults<Recipe> result = mongoTemplate.aggregate(agr, "recipes", Recipe.class);
        return result.getMappedResults();
    }



}
