package com.mealplannerv2.plangenerator.recipefilter.infrastructure.db.querybuilder;

import com.mealplannerv2.plangenerator.recipefilter.model.Ingredient;
import com.mealplannerv2.product.productkeeper.ProductKeeperFacade;
import lombok.Getter;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Component
public class QueryMaker {

    List<AggregationOperation> combinedOperations = new ArrayList<>();

    public void setUserProducts(List<Ingredient> ingredients, Criteria namesAmountCriteria) {
        if (!ProductKeeperFacade.productsInUse.isEmpty() || !ingredients.isEmpty()) {
//            if (userProducts != null) {


            List<AggregationOperation> productsToUseAgr = Arrays.asList(
                    Aggregation.match(namesAmountCriteria),
                    Aggregation.project()
                            .andInclude("name", "portions", "prepare_time", "max_storage_time", "diet", "ingredients", "steps")
                            .and(ArrayOperators.arrayOf(
                                    SetOperators.SetIntersection.arrayAsSet("ingredients")
                                            .intersects(LiteralOperators.Literal.asLiteral(ingredients))
                            ).length())
                            .as("matchingIngredientsCount"),
                    Aggregation.group("matchingIngredientsCount")
                            .push(Aggregation.ROOT).as("recipes"),
//                    Aggregation.sort(Sort.Direction.DESC, "_id"),
                    Aggregation.sort(Sort.Direction.DESC, "matchingIngredientsCount"),
                    Aggregation.limit(1),
                    Aggregation.unwind("$recipes"),
                    Aggregation.replaceRoot("$recipes")
            );

            combinedOperations.addAll(productsToUseAgr);
//            }
        }
    }

    public void setMaxStorageTime(int daysNr) {
        if (daysNr != 0) {
            Criteria maxStorageTimeCriteria = Criteria.where("max_storage_time").gte(daysNr);
            combinedOperations.add(Aggregation.match(maxStorageTimeCriteria));
        }
    }

    public void setDiet(String diet) {
        if (diet != null) {
            Criteria dietCriteria = Criteria.where("diet").is(diet);
            combinedOperations.add(Aggregation.match(dietCriteria));
        }
    }

    public void setPrepareTime(int time) {
        if (time != 0) {
            Criteria prepareTimeCriteria = Criteria.where("prepare_time").lte(time);
            combinedOperations.add(Aggregation.match(prepareTimeCriteria));
        }
    }

    public void setProductsToAvoid(List<String> productsToAvoid) {
        if (productsToAvoid != null) {
            Criteria productsToAvoidCriteria = Criteria.where("ingredients.name").nin(productsToAvoid);
            combinedOperations.add(Aggregation.match(productsToAvoidCriteria));
        }
    }

    public Aggregation getAggregation() {
        return Aggregation.newAggregation(combinedOperations);
    }

    public void clearOperationsList() {
        combinedOperations.clear();
    }
}
