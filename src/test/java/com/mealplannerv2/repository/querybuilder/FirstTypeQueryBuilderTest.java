package com.mealplannerv2.repository.querybuilder;


import com.mealplannerv2.repository.IngredientDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class FirstTypeQueryBuilderTest {

    QueryBuilder builder = new FirstTypeQueryBuilder();
    List<AggregationOperation> combinedOperationsTest;

    @BeforeEach
    void setUp() {
        combinedOperationsTest = QueryBuilder.combinedOperations;
        combinedOperationsTest.clear();
    }

    @Test
    void should_not_add_maxStorageTime_to_list() {
        builder.setMaxStorageTime(0);
        assertThat(combinedOperationsTest).isEmpty();
    }

    @Test
    void should_not_add_diet_to_list() {
        builder.setDiet(null);
        assertThat(combinedOperationsTest).isEmpty();
    }

    @Test
    void should_not_add_prepareRime_to_list() {
        builder.setPrepareTime(0);
        assertThat(combinedOperationsTest).isEmpty();
    }

    @Test
    void should_not_add_userProducts_to_list() {
        builder.setUserProducts(null ,1);
        assertThat(combinedOperationsTest).isEmpty();
    }

    @Test
    void should_not_add_productsToAvoid_to_list() {
        builder.setProductsToAvoid(null);
        assertThat(combinedOperationsTest).isEmpty();
    }

    @Test
    void should_not_add_any_of_arguments_to_list() {
        builder.setMaxStorageTime(0);
        builder.setDiet(null);
        builder.setPrepareTime(0);
        builder.setUserProducts(null ,1);
        builder.setProductsToAvoid(null);

        assertThat(combinedOperationsTest).isEmpty();
    }

    @Test
    void should_add_only_storageTime_and_diet_to_list() {
        builder.setMaxStorageTime(2);
        builder.setDiet("diet");
        builder.setPrepareTime(0);
        builder.setUserProducts(null ,1);
        builder.setProductsToAvoid(null);

        assertThat(combinedOperationsTest).size().isEqualTo(2);
    }

    @Test
    void should_add_all_arguments_to_list() {
        builder.setMaxStorageTime(2);
        builder.setDiet("diet");
        builder.setPrepareTime(15);
        builder.setUserProducts(List.of(new IngredientDto("product", 0.0, "")) ,1);
        builder.setProductsToAvoid(List.of("product"));

        assertThat(combinedOperationsTest).size().isEqualTo(5);
    }
}