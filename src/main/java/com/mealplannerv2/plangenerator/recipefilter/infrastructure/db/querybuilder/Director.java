package com.mealplannerv2.plangenerator.recipefilter.infrastructure.db.querybuilder;

import com.mealplannerv2.plangenerator.DataForRecipeFiltering;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Component
public class Director {

    private final QueryMaker queryMaker;
    private final NACriteriaBuilder namesAmountsCriteria = new NamesAmountsCriteria();
    private final NACriteriaBuilder onlyNamesCriteria = new OnlyNamesCriteria();
    private final List<Aggregation> aggregations = new ArrayList<>();

    public List<Aggregation> namesAndAmounts(DataForRecipeFiltering info){
        aggregations.clear();
        aggregations.add(firstTry(info, namesAmountsCriteria));
        aggregations.add(secondTry(info, namesAmountsCriteria));
        aggregations.add(thirdTry(info, namesAmountsCriteria));
//        aggregations.add(fourthTry(info));
        return aggregations;
    }

    public List<Aggregation> names(DataForRecipeFiltering info){
        aggregations.clear();
        aggregations.add(firstTry(info, onlyNamesCriteria));
        aggregations.add(secondTry(info, onlyNamesCriteria));
        aggregations.add(thirdTry(info, onlyNamesCriteria));
        aggregations.add(fourthTry(info));
        return aggregations;
    }

    Aggregation fourthTry(DataForRecipeFiltering info){
        queryMaker.clearOperationsList();
        queryMaker.setMaxStorageTime(info.forHowManyDays());
        queryMaker.setDiet(info.diet());
        return queryMaker.getAggregation();
    }

    Aggregation thirdTry(DataForRecipeFiltering info, NACriteriaBuilder criteria){
        fourthTry(info);
        queryMaker.setIngredientsToUseFirstly(info.ingredientsToUseFirstly(), criteria.getCriteria(info.ingredientsToUseFirstly()));
        return queryMaker.getAggregation();
    }

    Aggregation secondTry(DataForRecipeFiltering info, NACriteriaBuilder criteria){
        thirdTry(info, criteria);
        queryMaker.setPrepareTime(info.timeForPrepareMin());
        return queryMaker.getAggregation();
    }

    Aggregation firstTry(DataForRecipeFiltering info, NACriteriaBuilder criteria){
        secondTry(info, criteria);
        queryMaker.setProductsToAvoid(info.productsToAvoid());
        return queryMaker.getAggregation();
    }
}
