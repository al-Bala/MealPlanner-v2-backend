package com.mealplannerv2.repository;

import com.mealplannerv2.InfoForFiltering;
import com.mealplannerv2.entity.Recipe;
import com.mealplannerv2.repository.querybuilder.Director;
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
public class MealsFilterRepositoryImpl implements MealsFilterRepository{
    private final MongoTemplate mongoTemplate;
    private final Director director;

    public List<Recipe> findRecipesWithMatchingIngNamesAndAmounts(InfoForFiltering info) {
        List<Aggregation> aggregations = director.namesAndAmounts(info);
        return fetchRecipes(aggregations);
    }

    public List<Recipe> findRecipesWithMatchingIngNames(InfoForFiltering info) {
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
        System.out.println("AGR:\n" + agr);
        AggregationResults<Recipe> result = mongoTemplate.aggregate(agr, "recipes", Recipe.class);
        return result.getMappedResults();
    }



}
