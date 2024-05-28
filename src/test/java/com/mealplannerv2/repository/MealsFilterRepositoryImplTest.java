package com.mealplannerv2.repository;

import com.mealplannerv2.InfoForFiltering;
import com.mealplannerv2.entity.Recipe;
import com.mealplannerv2.repository.querybuilder.Director;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;

@ExtendWith(MockitoExtension.class)
public class MealsFilterRepositoryImplTest {

    @Mock
    private MongoTemplate mongoTemplate;

    @Mock
    private Director director;

    private MealsFilterRepositoryImpl mealsFilterRepository;

    private final InfoForFiltering info = InfoForFiltering.builder().build();

    @BeforeEach
    void setUp() {
        mealsFilterRepository = new MealsFilterRepositoryImpl(mongoTemplate, director);

        List<AggregationOperation> criteria = new ArrayList<>();
        criteria.add(Aggregation.match(Criteria.where("")));
        when(director.firstTry(info)).thenReturn(newAggregation(criteria));
        when(director.secondTry(info)).thenReturn(newAggregation(criteria));
        when(director.thirdTry(info)).thenReturn(newAggregation(criteria));
        when(director.fourthTry(info)).thenReturn(newAggregation(criteria));
    }

    @Test
    void should_make_3_tries_when_there_are_recipe_with_matching_storageTime_diet_productToUse() {
        List<Recipe> recipes2 = Collections.emptyList();
        List<Recipe> recipes = List.of(Recipe.builder().build());
        when(mongoTemplate.aggregate(any(Aggregation.class), anyString(), any(Class.class)))
                .thenReturn(new AggregationResults<>(recipes2, new Document()));
        when(mongoTemplate.aggregate(director.thirdTry(info), "recipes", Recipe.class))
                .thenReturn(new AggregationResults<>(recipes, new Document()));

        mealsFilterRepository.findMatchingRecipes(info);
        verify(mongoTemplate, times(3)).aggregate(any(Aggregation.class), anyString(), any(Class.class));
    }

    @Test
    void should_make_4_tries_when_there_are_not_any_matching_recipe() {
        List<Recipe> recipes2 = Collections.emptyList();
        when(mongoTemplate.aggregate(any(Aggregation.class), anyString(), any(Class.class)))
                .thenReturn(new AggregationResults<>(recipes2, new Document()));

        mealsFilterRepository.findMatchingRecipes(info);
        verify(mongoTemplate, times(4)).aggregate(any(Aggregation.class), anyString(), any(Class.class));
    }
}
