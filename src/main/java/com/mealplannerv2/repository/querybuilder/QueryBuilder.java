package com.mealplannerv2.repository.querybuilder;

import com.mealplannerv2.repository.IngredientDto;
import lombok.Getter;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;

import java.util.ArrayList;
import java.util.List;

public interface QueryBuilder {

    List<AggregationOperation> combinedOperations = new ArrayList<>();

    void setMaxStorageTime(int daysNr);
    void setDiet(String diet);
    void setPrepareTime(int time);
    void setUserProducts(List<IngredientDto> userProducts, int limit);
    void setProductsToAvoid(List<String> productsToAvoid);
    Aggregation getAggregation();
    void clearOperationsList();

}
