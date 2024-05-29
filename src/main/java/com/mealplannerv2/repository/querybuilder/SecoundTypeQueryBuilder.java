package com.mealplannerv2.repository.querybuilder;

import com.mealplannerv2.repository.IngredientDto;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SecoundTypeQueryBuilder extends FirstTypeQueryBuilder implements QueryBuilder {

    @Override
    public void setUserProducts(List<IngredientDto> userProducts, int limit) {
        if (userProducts != null) {
            //        Criteria criteria = Criteria.where("ingredients")
//                .elemMatch(
//                        Criteria.where("name").is("ryż"));
            Criteria criteria = new Criteria().andOperator(
                    getCriteria(userProducts)
            );
            combinedOperations.add(Aggregation.match(criteria));
        }
    }

    private static List<Criteria> getCriteria(List<IngredientDto> userProducts) {
        List<Criteria> c = new ArrayList<>();
        for(IngredientDto i: userProducts){
            c.add(Criteria.where("ingredients").elemMatch(Criteria.where("name").is(i.getName())));
        }
        return c;
    }

    @Override
    public void setMaxStorageTime(int daysNr) {
        super.setMaxStorageTime(daysNr);
    }

    @Override
    public void setDiet(String diet) {
        super.setDiet(diet);
    }

    @Override
    public void setPrepareTime(int time) {
        super.setPrepareTime(time);
    }

    @Override
    public void setProductsToAvoid(List<String> productsToAvoid) {
        super.setProductsToAvoid(productsToAvoid);
    }

    @Override
    public Aggregation getAggregation() {
        return super.getAggregation();
    }

    @Override
    public void clearOperationsList() {
        super.clearOperationsList();
    }
}
