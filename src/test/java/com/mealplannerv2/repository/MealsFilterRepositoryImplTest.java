package com.mealplannerv2.repository;

import com.mealplannerv2.InfoForFiltering;
import com.mealplannerv2.repository.querybuilder.Director;
import com.mealplannerv2.repository.querybuilder.QueryMaker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class MealsFilterRepositoryImplTest {

    @Mock
    private MongoTemplate mongoTemplate;

    @Mock
    QueryMaker queryMaker;

    @Mock
    private Director director;

    private MealsFilterRepositoryImpl mealsFilterRepository;

    private final InfoForFiltering info = InfoForFiltering.builder().build();

    @BeforeEach
    void setUp() {
//        mealsFilterRepository = new MealsFilterRepositoryImpl(mongoTemplate, director);
//
//        List<Aggregation> aggregations = new ArrayList<>();
//        for(int i = 0; i < 4; i++){
//            List<AggregationOperation> criteria = new ArrayList<>();
//            aggregations.add(Aggregation.newAggregation(criteria));
//        }
//        when(director.namesAndAmounts(info)).thenReturn(aggregations);
//        when(director.firstTry(info)).thenReturn(newAggregation(criteria));
//        when(director.secondTry(info)).thenReturn(newAggregation(criteria));
//        when(director.thirdTry(info)).thenReturn(newAggregation(criteria));
//        when(director.fourthTry(info)).thenReturn(newAggregation(criteria));
    }

//    @Test
//    void should_make_3_tries_when_there_are_recipe_with_matching_storageTime_diet_productToUse() {
//        List<Recipe> recipes2 = Collections.emptyList();
//        List<Recipe> recipes = List.of(Recipe.builder().build());
//        when(mongoTemplate.aggregate(any(Aggregation.class), anyString(), any(Class.class)))
//                .thenReturn(new AggregationResults<>(recipes2, new Document()));
//        when(mongoTemplate.aggregate(director.thirdTry(info, firstTypeQueryBuilder), "recipes", Recipe.class))
//                .thenReturn(new AggregationResults<>(recipes, new Document()));
//
//        mealsFilterRepository.findRecipesWithMatchingIngNamesAndAmounts(info);
//        verify(mongoTemplate, times(3)).aggregate(any(Aggregation.class), anyString(), any(Class.class));
//    }
//
//    @Test
//    void should_make_4_tries_when_there_are_not_any_matching_recipe() {
//        List<Recipe> recipes2 = Collections.emptyList();
//        when(mongoTemplate.aggregate(any(Aggregation.class), anyString(), any(Class.class)))
//                .thenReturn(new AggregationResults<>(recipes2, new Document()));
//
//        mealsFilterRepository.findRecipesWithMatchingIngNamesAndAmounts(info);
//        verify(mongoTemplate, times(4)).aggregate(any(Aggregation.class), anyString(), any(Class.class));
//    }
}
