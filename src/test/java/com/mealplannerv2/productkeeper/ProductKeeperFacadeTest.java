package com.mealplannerv2.productkeeper;

import com.mealplannerv2.product.productkeeper.IngredientDtoComparator;
import com.mealplannerv2.product.productkeeper.ProductKeeperFacade;
import com.mealplannerv2.plangenerator.recipefilter.dto.IngredientDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.TreeSet;

import static com.mealplannerv2.product.productkeeper.ProductKeeperFacade.productsInUse;
import static org.assertj.core.api.Assertions.assertThat;

class ProductKeeperFacadeTest {

    @BeforeEach
    void setUp() {
        IngredientDtoComparator comparator = new IngredientDtoComparator();
        productsInUse = new TreeSet<>(comparator);
    }

    @Test
    void should_sort_ingredients_from_oldest_to_earliest_date() {
        // given
        IngredientDto ing19 = new IngredientDto("name1", 0.0, "", LocalDate.of(2024, 5, 19));
        IngredientDto ing20 = new IngredientDto("name2", 0.0, "", LocalDate.of(2024, 5, 20));
        IngredientDto ing18 = new IngredientDto("name3", 0.0, "", LocalDate.of(2024, 5, 18));

        //when
        productsInUse.add(ing19);
        productsInUse.add(ing20);
        productsInUse.add(ing18);

        //then
        System.out.println(productsInUse);
        assertThat(productsInUse).containsExactly(ing18, ing19, ing20);
    }

    @Test
    void should_sort_in_des_order_of_amount_for_ingredients_with_the_same_date() {
        // given
        IngredientDto ing19 = new IngredientDto("name1", 10.0, "", LocalDate.of(2024, 5, 19));
        IngredientDto ing18 = new IngredientDto("name2", 20.0, "", LocalDate.of(2024, 5, 18));
        IngredientDto ing18_2 = new IngredientDto("name3", 5.0, "", LocalDate.of(2024, 5, 18));

        //when
        productsInUse.add(ing19);
        productsInUse.add(ing18);
        productsInUse.add(ing18_2);

        //then
        System.out.println(productsInUse);
        assertThat(productsInUse).containsExactly(ing18, ing18_2, ing19);
    }

    @Test
    void should_sum_amount_of_ingredients_with_the_same_name() {
        // given
        IngredientDto ing1 = new IngredientDto("name1", 10.0, "");
        IngredientDto ing2 = new IngredientDto("name2", 1.0, "");
        IngredientDto ing3 = new IngredientDto("name1", 10.0, "");

        //when
        ProductKeeperFacade.addUniq(ing1);
        ProductKeeperFacade.addUniq(ing2);
        ProductKeeperFacade.addUniq(ing3);

        //then
        System.out.println(productsInUse);
        assertThat(productsInUse).containsExactly(new IngredientDto("name1", 20.0, ""), ing2);
    }
}