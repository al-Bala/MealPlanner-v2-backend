package com.mealplannerv2.repository.querybuilder;

import com.mealplannerv2.repository.IngredientDto;
import org.springframework.data.mongodb.core.aggregation.Aggregation;

import java.util.List;

public interface QueryBuilder {

    void setMaxStorageTime(int daysNr);
    void setDiet(String diet);
    void setPrepareTime(int time);
    void setUserProducts(List<IngredientDto> userProducts, int limit);
    void setProductsToAvoid(List<String> productsToAvoid);
    Aggregation getAggregation();
    void clearOperationsList();

}
