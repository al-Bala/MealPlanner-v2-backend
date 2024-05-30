package com.mealplannerv2.repository.querybuilder;

import com.mealplannerv2.InfoForFiltering;
import com.mealplannerv2.repository.IngredientDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.aggregation.Aggregation;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class DirectorTest {

    QueryMaker queryMaker = new QueryMaker();
    NACriteriaBuilder namesAmountsCriteria = new NamesAmountsCriteria();
    NACriteriaBuilder onlyNamesCriteria = new OnlyNamesCriteria();
    Director director = new Director(queryMaker);

    InfoForFiltering info;

    @BeforeEach
    void setUp(){
        info = InfoForFiltering.builder()
                .forHowManyDays(1)
                .diet("diet")
                .timeForPrepareMin(1)
                .userProducts(List.of(new IngredientDto("productU", 0.0, "")))
                .productsToAvoid(List.of("productA"))
                .build();
    }

    @Test
    void should_include_all_arguments() {
        // when
        Aggregation agrFirstType = director.firstTry(info, namesAmountsCriteria);
        Aggregation agrSecoundType = director.firstTry(info, onlyNamesCriteria);
        // then
        List<String> expectedValues = List.of("max_storage_time", "diet", "productU", "prepare_time", "productA");

        System.out.println(agrFirstType);
        assertThat(agrFirstType.toString()).contains(expectedValues);
        assertThat(agrSecoundType.toString()).contains(expectedValues);
    }

    @Test
    void should_omit_productsToAvoid() {
        // when
        Aggregation agrFirstType = director.secondTry(info, namesAmountsCriteria);
        Aggregation agrSecoundType = director.secondTry(info, onlyNamesCriteria);
        // then
        List<String> expectedValues = List.of("max_storage_time", "diet", "productU", "prepare_time");

        assertThat(agrFirstType.toString()).contains(expectedValues);
        assertThat(agrFirstType.toString()).doesNotContain("productA");

        assertThat(agrSecoundType.toString()).contains(expectedValues);
        assertThat(agrSecoundType.toString()).doesNotContain("productA");
    }

    @Test
    void should_omit_productsToAvoid_and_prepareTime() {
        // when
        Aggregation agrFirstType = director.thirdTry(info, namesAmountsCriteria);
        Aggregation agrSecoundType = director.thirdTry(info, onlyNamesCriteria);
        // then
        List<String> expectedValues = List.of("max_storage_time", "diet", "productU");

        assertThat(agrFirstType.toString()).contains(expectedValues);
        assertThat(agrFirstType.toString()).doesNotContain("productA");
        assertThat(agrFirstType.toString()).containsOnlyOnce("prepare_time");

        assertThat(agrSecoundType.toString()).contains(expectedValues);
        assertThat(agrSecoundType.toString()).doesNotContain("productA");
        assertThat(agrSecoundType.toString()).containsOnlyOnce("prepare_time");
    }

    @Test
    void should_omit_productsToAvoid_and_prepareTime_and_userProducts() {
        // when
        Aggregation agrFirstType = director.fourthTry(info);
        Aggregation agrSecoundType = director.fourthTry(info);
        // then
        List<String> expectedValues = List.of("max_storage_time", "diet");

        assertThat(agrFirstType.toString()).contains(expectedValues);
        assertThat(agrFirstType.toString()).doesNotContain(List.of("productA", "prepare_time", "productU"));

        assertThat(agrSecoundType.toString()).contains(expectedValues);
        assertThat(agrSecoundType.toString()).doesNotContain(List.of("productA", "prepare_time", "productU"));
    }
}
