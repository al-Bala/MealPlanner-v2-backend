package com.mealplannerv2.plangenerator.recipefilter.infrastructure.db.querybuilder;

import com.mealplannerv2.plangenerator.InfoFiltering2;
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

    public List<Aggregation> namesAndAmounts(InfoFiltering2 info){
        aggregations.clear();
        aggregations.add(firstTry(info, namesAmountsCriteria));
        aggregations.add(secondTry(info, namesAmountsCriteria));
        aggregations.add(thirdTry(info, namesAmountsCriteria));
//        aggregations.add(fourthTry(info));
        return aggregations;
    }

    public List<Aggregation> names(InfoFiltering2 info){
        aggregations.clear();
        aggregations.add(firstTry(info, onlyNamesCriteria));
        aggregations.add(secondTry(info, onlyNamesCriteria));
        aggregations.add(thirdTry(info, onlyNamesCriteria));
        aggregations.add(fourthTry(info));
        return aggregations;
    }

    Aggregation fourthTry(InfoFiltering2 info){
        queryMaker.clearOperationsList();
        queryMaker.setMaxStorageTime(info.forHowManyDays());
        queryMaker.setDiet(info.diet());
        return queryMaker.getAggregation();
    }

    Aggregation thirdTry(InfoFiltering2 info, NACriteriaBuilder criteria){
        fourthTry(info);
        queryMaker.setUserProducts(info.userProducts(), criteria.getCriteria(info.userProducts()));
        return queryMaker.getAggregation();
    }

    Aggregation secondTry(InfoFiltering2 info, NACriteriaBuilder criteria){
        thirdTry(info, criteria);
        queryMaker.setPrepareTime(info.timeForPrepareMin());
        return queryMaker.getAggregation();
    }

    Aggregation firstTry(InfoFiltering2 info, NACriteriaBuilder criteria){
        secondTry(info, criteria);
        queryMaker.setProductsToAvoid(info.productsToAvoid());
        return queryMaker.getAggregation();
    }
}
