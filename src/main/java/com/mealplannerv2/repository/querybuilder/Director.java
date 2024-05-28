package com.mealplannerv2.repository.querybuilder;

import com.mealplannerv2.InfoForFiltering;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class Director {

    private final QueryBuilder builder;

    public Aggregation firstTry(InfoForFiltering info){
        builder.clearOperationsList();
        builder.setMaxStorageTime(info.forHowManyDays());
        builder.setDiet(info.diet());
        builder.setUserProducts(info.userProducts(), 1);
        builder.setPrepareTime(info.timeForPrepareMin());
        builder.setProductsToAvoid(info.productsToAvoid());
        return builder.getAggregation();
    }

    public Aggregation secondTry(InfoForFiltering info){
        builder.clearOperationsList();
        builder.setMaxStorageTime(info.forHowManyDays());
        builder.setDiet(info.diet());
        builder.setUserProducts(info.userProducts(), 1);
        builder.setPrepareTime(info.timeForPrepareMin());
        return builder.getAggregation();
    }

    public Aggregation thirdTry(InfoForFiltering info){
        builder.clearOperationsList();
        builder.setMaxStorageTime(info.forHowManyDays());
        builder.setDiet(info.diet());
        builder.setUserProducts(info.userProducts(), 1);
        return builder.getAggregation();
    }

    public Aggregation fourthTry(InfoForFiltering info){
        builder.clearOperationsList();
        builder.setMaxStorageTime(info.forHowManyDays());
        builder.setDiet(info.diet());
        return builder.getAggregation();
    }
}
