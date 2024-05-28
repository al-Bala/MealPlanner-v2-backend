package com.mealplannerv2.repository.querybuilder;


import com.mealplannerv2.repository.IngredientDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class QueryPreferencesTest {

    QueryPreferences queryPreferences = new QueryPreferences();
    List<AggregationOperation> combinedOperations;

    @BeforeEach
    void setUp() {
        combinedOperations = queryPreferences.getCombinedOperations();
    }

    @Test
    void should_not_add_maxStorageTime_to_list() {
        queryPreferences.setMaxStorageTime(0);
        assertThat(combinedOperations).isEmpty();
    }

    @Test
    void should_not_add_diet_to_list() {
        queryPreferences.setDiet(null);
        assertThat(combinedOperations).isEmpty();
    }

    @Test
    void should_not_add_prepareRime_to_list() {
        queryPreferences.setPrepareTime(0);
        assertThat(combinedOperations).isEmpty();
    }

    @Test
    void should_not_add_userProducts_to_list() {
        queryPreferences.setUserProducts(null ,1);
        assertThat(combinedOperations).isEmpty();
    }

    @Test
    void should_not_add_productsToAvoid_to_list() {
        queryPreferences.setProductsToAvoid(null);
        assertThat(combinedOperations).isEmpty();
    }

    @Test
    void should_not_add_any_of_arguments_to_list() {
        queryPreferences.setMaxStorageTime(0);
        queryPreferences.setDiet(null);
        queryPreferences.setPrepareTime(0);
        queryPreferences.setUserProducts(null ,1);
        queryPreferences.setProductsToAvoid(null);

        assertThat(combinedOperations).isEmpty();
    }

    @Test
    void should_add_only_storageTime_and_diet_to_list() {
        queryPreferences.setMaxStorageTime(2);
        queryPreferences.setDiet("diet");
        queryPreferences.setPrepareTime(0);
        queryPreferences.setUserProducts(null ,1);
        queryPreferences.setProductsToAvoid(null);

        assertThat(combinedOperations).size().isEqualTo(2);
    }

    @Test
    void should_add_all_arguments_to_list() {
        queryPreferences.setMaxStorageTime(2);
        queryPreferences.setDiet("diet");
        queryPreferences.setPrepareTime(15);
        queryPreferences.setUserProducts(List.of(new IngredientDto("product", 0.0, "")) ,1);
        queryPreferences.setProductsToAvoid(List.of("product"));

        assertThat(combinedOperations).size().isEqualTo(5);
    }
}