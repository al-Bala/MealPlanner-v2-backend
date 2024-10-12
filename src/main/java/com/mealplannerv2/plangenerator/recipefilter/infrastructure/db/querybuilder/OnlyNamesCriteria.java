package com.mealplannerv2.plangenerator.recipefilter.infrastructure.db.querybuilder;

import com.mealplannerv2.recipe.model.Ingredient;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OnlyNamesCriteria implements NACriteriaBuilder {

    @Override
    public Criteria getCriteria(List<Ingredient> userProducts) {
        if(userProducts == null){
            return null;
        }
        return Criteria.where("ingredients").elemMatch(
                new Criteria().orOperator(getCriteriaList(userProducts)));
    }

    private static List<Criteria> getCriteriaList(List<Ingredient> userProducts) {
        List<Criteria> c = new ArrayList<>();
        for (Ingredient i : userProducts) {
//            c.add(Criteria.where("ingredients").elemMatch(Criteria.where("name").is(i.getName())));
            c.add(Criteria.where("name").is(i.name()));
        }
        return c;
    }
}
