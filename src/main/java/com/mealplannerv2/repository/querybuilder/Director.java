package com.mealplannerv2.repository.querybuilder;

import com.mealplannerv2.InfoForFiltering;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Component
public class Director {

    private final FirstTypeQueryBuilder firstTypeQueryBuilder;
    private final SecoundTypeQueryBuilder secoundTypeQueryBuilder;
    private final List<Aggregation> aggregations = new ArrayList<>();

    public List<Aggregation> namesAndAmounts(InfoForFiltering info){
        aggregations.clear();
        aggregations.add(firstTry(info, firstTypeQueryBuilder));
        aggregations.add(secondTry(info, firstTypeQueryBuilder));
        aggregations.add(thirdTry(info, firstTypeQueryBuilder));
        aggregations.add(fourthTry(info, firstTypeQueryBuilder));
        return aggregations;
    }

    public List<Aggregation> names(InfoForFiltering info){
        aggregations.clear();
        aggregations.add(firstTry(info, secoundTypeQueryBuilder));
        aggregations.add(secondTry(info, secoundTypeQueryBuilder));
        aggregations.add(thirdTry(info, secoundTypeQueryBuilder));
        aggregations.add(fourthTry(info, secoundTypeQueryBuilder));
        return aggregations;
    }

    Aggregation fourthTry(InfoForFiltering info, QueryBuilder queryBuilder){
        queryBuilder.clearOperationsList();
        queryBuilder.setMaxStorageTime(info.forHowManyDays());
        queryBuilder.setDiet(info.diet());
        return queryBuilder.getAggregation();
    }

    Aggregation thirdTry(InfoForFiltering info, QueryBuilder queryBuilder){
        fourthTry(info, queryBuilder);
//        builder.clearOperationsList();
//        builder.setMaxStorageTime(info.forHowManyDays());
//        builder.setDiet(info.diet());
        queryBuilder.setUserProducts(info.userProducts(), 1);
        return queryBuilder.getAggregation();
    }

    Aggregation secondTry(InfoForFiltering info, QueryBuilder queryBuilder){
        thirdTry(info, queryBuilder);
//        builder.clearOperationsList();
//        builder.setMaxStorageTime(info.forHowManyDays());
//        builder.setDiet(info.diet());
//        builder.setUserProducts(info.userProducts(), 1);
        queryBuilder.setPrepareTime(info.timeForPrepareMin());
        return queryBuilder.getAggregation();
    }

    Aggregation firstTry(InfoForFiltering info, QueryBuilder queryBuilder){
        secondTry(info, queryBuilder);
//        builder.clearOperationsList();
//        builder.setMaxStorageTime(info.forHowManyDays());
//        builder.setDiet(info.diet());
//        builder.setUserProducts(info.userProducts(), 1);
//        builder.setPrepareTime(info.timeForPrepareMin());
        queryBuilder.setProductsToAvoid(info.productsToAvoid());
        return queryBuilder.getAggregation();
    }
}
