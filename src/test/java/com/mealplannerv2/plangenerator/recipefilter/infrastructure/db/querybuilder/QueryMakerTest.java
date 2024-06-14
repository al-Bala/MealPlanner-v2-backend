package com.mealplannerv2.plangenerator.recipefilter.infrastructure.db.querybuilder;


import com.mealplannerv2.plangenerator.recipefilter.model.Ingredient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class QueryMakerTest {

    QueryMaker queryMaker = new QueryMaker();
    List<AggregationOperation> combinedOperationsTest;

    @BeforeEach
    void setUp() {
        combinedOperationsTest = queryMaker.getCombinedOperations();
        combinedOperationsTest.clear();
    }

    @Test
    void should_not_add_typeOfMeal_to_list() {
        queryMaker.setTypeOfMeal(null);
        assertThat(combinedOperationsTest).isEmpty();
    }

    @Test
    void should_not_add_maxStorageTime_to_list() {
        queryMaker.setMaxStorageTime(0);
        assertThat(combinedOperationsTest).isEmpty();
    }

    @Test
    void should_not_add_diet_to_list() {
        queryMaker.setDiet(null);
        assertThat(combinedOperationsTest).isEmpty();
    }

    @Test
    void should_not_add_prepareRime_to_list() {
        queryMaker.setPrepareTime(null);
        assertThat(combinedOperationsTest).isEmpty();
    }

    @Test
    void should_not_add_userProducts_to_list() {
        queryMaker.setIngredientsToUseFirstly(null ,new Criteria());
        assertThat(combinedOperationsTest).isEmpty();
    }

    @Test
    void should_save_7_operation_in_list() {
        queryMaker.setIngredientsToUseFirstly(List.of(new Ingredient("product", 0.0, "")), new Criteria());
        assertThat(combinedOperationsTest).size().isEqualTo(7);
    }

    @Test
    void should_not_add_productsToAvoid_to_list() {
        queryMaker.setProductsToAvoid(null);
        assertThat(combinedOperationsTest).isEmpty();
    }

    @Test
    void should_not_add_any_of_arguments_to_list() {
        queryMaker.setMaxStorageTime(0);
        queryMaker.setDiet(null);
        queryMaker.setPrepareTime(null);
        queryMaker.setIngredientsToUseFirstly(null ,new Criteria());
        queryMaker.setProductsToAvoid(null);

        assertThat(combinedOperationsTest).isEmpty();
    }

    @Test
    void should_add_only_storageTime_and_diet_to_list() {
        queryMaker.setMaxStorageTime(2);
        queryMaker.setDiet("diet");
        queryMaker.setPrepareTime(null);
        queryMaker.setIngredientsToUseFirstly(null ,new Criteria());
        queryMaker.setProductsToAvoid(null);

        assertThat(combinedOperationsTest).size().isEqualTo(2);
    }

    @Test
    void should_add_all_11_operations_to_list() {
        queryMaker.setMaxStorageTime(2);
        queryMaker.setDiet("diet");
        queryMaker.setPrepareTime(15);
        queryMaker.setIngredientsToUseFirstly(List.of(new Ingredient("product", 0.0, "")), new Criteria());
        queryMaker.setProductsToAvoid(List.of("product"));

        assertThat(combinedOperationsTest).size().isEqualTo(11);
    }
}