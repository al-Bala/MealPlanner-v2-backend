package com.mealplannerv2.repository.querybuilder;

import com.mealplannerv2.InfoForFiltering;
import com.mealplannerv2.repository.IngredientDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.aggregation.Aggregation;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class DirectorTest {

    QueryBuilder queryPreferences = new QueryPreferences();
    Director director = new Director(queryPreferences);

    InfoForFiltering info;

    @BeforeEach
    void setUp(){
        info = InfoForFiltering.builder()
                .forHowManyDays(1)
                .diet("diet")
                .timeForPrepareMin(1)
                .userProducts(List.of(new IngredientDto("productU", 0.0, "")))
                .productsToAvoid(List.of("productD"))
                .build();
    }

    @Test
    void should_include_all_arguments() {
        // when
        Aggregation aggregation = director.firstTry(info);

        // then
        String expectedPipeline = "{ \"aggregate\" : \"__collection__\", \"pipeline\" : [" +
                "{ \"$match\" : { \"max_storage_time\" : { \"$gte\" : 1}}}, " +
                "{ \"$match\" : { \"diet\" : \"diet\"}}, " +
                "{ \"$match\" : { \"ingredients.name\" : { \"$in\" : [\"productU\"]}}}, " +
                "{ \"$match\" : { \"prepare_time\" : { \"$lte\" : 1}}}, " +
                "{ \"$match\" : { \"ingredients.name\" : { \"$nin\" : [\"productD\"]}}}]}";

        assertThat(aggregation.toString()).isEqualTo(expectedPipeline);
    }

    @Test
    void should_omit_productsToAvoid() {
        // when
        Aggregation aggregation = director.secondTry(info);

        // then
        String expectedPipeline = "{ \"aggregate\" : \"__collection__\", \"pipeline\" : [" +
                "{ \"$match\" : { \"max_storage_time\" : { \"$gte\" : 1}}}, " +
                "{ \"$match\" : { \"diet\" : \"diet\"}}, " +
                "{ \"$match\" : { \"ingredients.name\" : { \"$in\" : [\"productU\"]}}}, " +
                "{ \"$match\" : { \"prepare_time\" : { \"$lte\" : 1}}}]}";

        assertThat(aggregation.toString()).isEqualTo(expectedPipeline);
    }

    @Test
    void should_omit_productsToAvoid_and_prepareTime() {
        // when
        Aggregation aggregation = director.thirdTry(info);

        // then
        String expectedPipeline = "{ \"aggregate\" : \"__collection__\", \"pipeline\" : [" +
                "{ \"$match\" : { \"max_storage_time\" : { \"$gte\" : 1}}}, " +
                "{ \"$match\" : { \"diet\" : \"diet\"}}, " +
                "{ \"$match\" : { \"ingredients.name\" : { \"$in\" : [\"productU\"]}}}]}";

        assertThat(aggregation.toString()).isEqualTo(expectedPipeline);
    }

    @Test
    void should_omit_productsToAvoid_and_prepareTime_and_userProducts() {
        // when
        Aggregation aggregation = director.fourthTry(info);
        System.out.println(aggregation);

        // then
        String expectedPipeline = "{ \"aggregate\" : \"__collection__\", \"pipeline\" : [" +
                "{ \"$match\" : { \"max_storage_time\" : { \"$gte\" : 1}}}, " +
                "{ \"$match\" : { \"diet\" : \"diet\"}}]}";

        assertThat(aggregation.toString()).isEqualTo(expectedPipeline);
    }
}
