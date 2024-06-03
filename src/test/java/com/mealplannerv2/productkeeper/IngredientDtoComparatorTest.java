package com.mealplannerv2.productkeeper;

import com.mealplannerv2.product.productkeeper.IngredientDtoComparator;
import com.mealplannerv2.plangenerator.recipefilter.dto.IngredientDto;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

class IngredientDtoComparatorTest {

    private final IngredientDtoComparator comparator = new IngredientDtoComparator();

    @Test
    void compare() {
        // given
        IngredientDto ing1 = new IngredientDto("name1", 0.0, "", LocalDate.of(2024, 5, 19));
        IngredientDto ing2 = new IngredientDto("name2", 0.0, "", LocalDate.of(2024, 5, 21));

        // when
        int compare = comparator.compare(ing1, ing2);

    }
}