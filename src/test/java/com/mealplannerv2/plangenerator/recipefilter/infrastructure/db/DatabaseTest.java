package com.mealplannerv2.plangenerator.recipefilter.infrastructure.db;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
public class DatabaseTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @BeforeEach
    void setUp() throws IOException {
        Resource resource = new ClassPathResource("test-recipes-data.json");
        byte[] jsonBytes = FileCopyUtils.copyToByteArray(resource.getInputStream());
        String jsonData = new String(jsonBytes, StandardCharsets.UTF_8);

        ObjectMapper objectMapper = new ObjectMapper();
        List<RecipeTest> documents = Arrays.asList(objectMapper.readValue(jsonData, RecipeTest[].class));

        mongoTemplate.insertAll(documents);
    }

    @Test
    void find_diet() {
        // given
        Criteria criteria = Criteria.where("diet").is("mięsna");
        // when
        List<RecipeTest> queryResult = makeQuery(criteria);
        List<String> names = getRecipeNames(queryResult);
        // then
        List<String> expectedIds = List.of("Ryż z warzywami i kurczakiem");
        assertThat(names).isEqualTo(expectedIds);
    }

    @Test
    void find_prepareTime() {
        // given
        Criteria criteria = Criteria.where("prepare_time").lte(10);
        // when
        List<RecipeTest> queryResult = makeQuery(criteria);
        List<String> names = getRecipeNames(queryResult);
        // then
        List<String> expectedIds = List.of("Koktajl owocowy");
        assertThat(names).isEqualTo(expectedIds);
    }

    @Test
    void find_typeOfMeal() {
        // given
        Criteria criteria = Criteria.where("type_of_meal").is("breakfast");
        // when
        List<RecipeTest> queryResult = makeQuery(criteria);
        List<String> names = getRecipeNames(queryResult);
        // then
        List<String> expectedIds = List.of("Sałatka owocowa", "Jajecznica z warzywami", "Koktajl owocowy");
        assertThat(names).isEqualTo(expectedIds);
    }

    @AfterEach
    void tearDown() {
        mongoTemplate.dropCollection("recipes-test");
    }

    private List<RecipeTest> makeQuery(Criteria criteria) {
        Query query = new Query();
        query.addCriteria(criteria);
        return mongoTemplate.find(query, RecipeTest.class);
    }

    private List<String> getRecipeNames(List<RecipeTest> list) {
        return list.stream()
                .map(RecipeTest::name)
                .toList();
    }
}
