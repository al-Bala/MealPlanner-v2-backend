package com.mealplannerv2.repository.querybuilder;

import com.mealplannerv2.repository.IngredientDto;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class NamesAmountsCriteria implements NACriteriaBuilder {

    @Override
    public Criteria getCriteria(List<IngredientDto> userProducts) {
        return Criteria.where("ingredients").elemMatch(
                new Criteria().orOperator(getCriteriaList(userProducts)));
    }

    private static List<Criteria> getCriteriaList(List<IngredientDto> userProducts) {
        List<Criteria> c = new ArrayList<>();
        for (IngredientDto i : userProducts) {
            c.add(Criteria.where("name").is(i.getName()).and("amount").is(i.getAmount()));
        }
        return c;
    }
}
