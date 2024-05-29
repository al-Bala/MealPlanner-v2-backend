package com.mealplannerv2.repository.querybuilder;

import com.mealplannerv2.product.ProductGrouper;
import com.mealplannerv2.product.ProductKeeperFacade;
import com.mealplannerv2.repository.IngredientDto;
import lombok.Getter;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FirstTypeQueryBuilder implements QueryBuilder {

    @Override
    public void setUserProducts(List<IngredientDto> userProducts, int limit) {
//        if (!ProductKeeperFacade.productsInUse.isEmpty()) {
        if (userProducts != null) {
            ProductKeeperFacade.addAllUniq(userProducts);

//            Criteria productsToUseCriteria = Criteria.where("ingredients")
//                    .elemMatch(
//                        Criteria.where("name").is("ryż")
//                                .and("amount").gte(100)
//                    );

            List<IngredientDto> prioritisedProducts = ProductGrouper.getAllWithGivenWaitingLevel(1);

            Criteria criteria = new Criteria().andOperator(
                    getCriteria(userProducts)
            );

//            List<AggregationOperation> productsToUseAgr = Arrays.asList(
//                    Aggregation.match(Criteria.where("ingredients.name").in(userProducts)),
//                    Aggregation.project()
////                            .andInclude("_id", "name", "ingredients")
//                            .andInclude("name", "portions", "prepare_time", "max_storage_time", "diet", "ingredients", "steps")
//                            .and(ArrayOperators.arrayOf(
//                                    SetOperators.SetIntersection.arrayAsSet("ingredients.name")
//                                            .intersects(LiteralOperators.Literal.asLiteral(userProducts))
//                            ).length())
//                            .as("matchingIngredientsCount"),
//                    Aggregation.group("matchingIngredientsCount")
//                            .push(Aggregation.ROOT).as("recipes"),
//                    Aggregation.sort(Sort.Direction.DESC, "_id"),
//                    Aggregation.limit(limit),
//                    Aggregation.unwind("$recipes"),
//                    Aggregation.replaceRoot("$recipes")
//            );

            combinedOperations.add(Aggregation.match(criteria));
        }
    }

    private static List<Criteria> getCriteria(List<IngredientDto> userProducts) {
        List<Criteria> c = new ArrayList<>();
        for(IngredientDto i: userProducts){
            c.add(Criteria.where("ingredients").elemMatch(Criteria.where("name").is(i.getName()).and("amount").is(i.getAmount())));
        }
        return c;
    }

    @Override
    public void setMaxStorageTime(int daysNr) {
        if (daysNr != 0) {
            Criteria maxStorageTimeCriteria = Criteria.where("max_storage_time").gte(daysNr);
            combinedOperations.add(Aggregation.match(maxStorageTimeCriteria));
        }
    }

    @Override
    public void setDiet(String diet) {
        if (diet != null) {
            Criteria dietCriteria = Criteria.where("diet").is(diet);
            combinedOperations.add(Aggregation.match(dietCriteria));
        }
    }

    @Override
    public void setPrepareTime(int time) {
        if (time != 0) {
            Criteria prepareTimeCriteria = Criteria.where("prepare_time").lte(time);
            combinedOperations.add(Aggregation.match(prepareTimeCriteria));
        }
    }

    @Override
    public void setProductsToAvoid(List<String> productsToAvoid) {
        if (productsToAvoid != null) {
            Criteria productsToAvoidCriteria = Criteria.where("ingredients.name").nin(productsToAvoid);
            combinedOperations.add(Aggregation.match(productsToAvoidCriteria));
        }
    }

    @Override
    public Aggregation getAggregation() {
        return Aggregation.newAggregation(combinedOperations);
    }

    @Override
    public void clearOperationsList() {
        combinedOperations.clear();
    }
}
