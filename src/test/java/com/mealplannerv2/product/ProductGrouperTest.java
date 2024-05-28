package com.mealplannerv2.product;

import com.mealplannerv2.repository.IngredientDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.TreeSet;

import static com.mealplannerv2.product.ProductKeeperFacade.productsInUse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ProductGrouperTest {

    @BeforeEach
    void setUp() {
        IngredientDtoComparator comparator = new IngredientDtoComparator();
        productsInUse = new TreeSet<>(comparator);

        IngredientDto ing1 = new IngredientDto("name1", 1.0, "", LocalDate.of(2024, 5, 19));
        IngredientDto ing2 = new IngredientDto("name2", 1.0, "", LocalDate.of(2024, 5, 18));
        IngredientDto ing3 = new IngredientDto("name3", 1.0, "", LocalDate.of(2024, 5, 18));
        ProductKeeperFacade.addAllUniq(List.of(ing1, ing2, ing3));
    }

    @Test
    void should_return_ingredients_with_latest_date_when_waitingLevel_is_1() {
        // when
        List<IngredientDto> allWithGivenDaysNumber = ProductGrouper.getAllWithGivenDaysNumber(1);
        // then
        System.out.println(allWithGivenDaysNumber);
        assertThat(allWithGivenDaysNumber).containsExactly(
                new IngredientDto("name2", 1.0, "", LocalDate.of(2024, 5, 18)),
                new IngredientDto("name3", 1.0, "", LocalDate.of(2024, 5, 18)));
    }

    @Test
    void should_return_ingredients_with_latest_date_when_waitingLevel_is_equal_or_less_than_0() {
        // when
        List<IngredientDto> allWithGivenDaysNumber = ProductGrouper.getAllWithGivenDaysNumber(-1);
        // then
        System.out.println(allWithGivenDaysNumber);
        assertThat(allWithGivenDaysNumber).containsExactly(
                new IngredientDto("name2", 1.0, "", LocalDate.of(2024, 5, 18)),
                new IngredientDto("name3", 1.0, "", LocalDate.of(2024, 5, 18)));
    }

    @Test
    void should_return_all_ingredients_when_waitingLevel_is_larger_than_number_of_groups() {
        // when
        List<IngredientDto> allWithGivenDaysNumber = ProductGrouper.getAllWithGivenDaysNumber(100);
        // then
        System.out.println(allWithGivenDaysNumber);
        assertThat(allWithGivenDaysNumber).containsExactly(
                new IngredientDto("name2", 1.0, "", LocalDate.of(2024, 5, 18)),
                new IngredientDto("name3", 1.0, "", LocalDate.of(2024, 5, 18)),
                new IngredientDto("name1", 1.0, "", LocalDate.of(2024, 5, 19)));
    }
}