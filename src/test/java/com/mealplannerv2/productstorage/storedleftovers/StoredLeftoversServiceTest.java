package com.mealplannerv2.productstorage.storedleftovers;

import com.mealplannerv2.plangenerator.recipefilter.dto.IngredientDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class StoredLeftoversServiceTest {

    StoredLeftoversService storedProductsService;
    private Map<String, StoredLeftoverDto> storedProducts;
    StoredLeftoverDto p1;

    @BeforeEach
    void setUp() {
        storedProductsService = new StoredLeftoversService();
        storedProducts = storedProductsService.getStoredProducts();
        storedProducts.clear();
        p1 = new StoredLeftoverDto("ing1", 100.0,"g");
        storedProducts.put(p1.getName(), p1);
    }

    @Test
    void should_sum_up_amounts_when_names_and_units_are_the_same() {
        // when
        storedProductsService.addUniq(p1);
        // then
        assertThat(storedProducts.get(p1.getName()).getAmount()).isEqualTo( 200.0);
    }

    @Test
    void should_add_new_ingredient_when_map_is_empty() {
        // given
        storedProducts.clear();
        // when
        storedProductsService.addUniq(p1);
        // then
        assertThat(storedProducts.get(p1.getName()).getAmount()).isEqualTo( 100.0);
    }

    @Test
    void should_add_new_ingredient_when_there_are_not_the_same_ingredient_in_map() {
        StoredLeftoverDto p2 = new StoredLeftoverDto("ing2", 180.0,"ml");
        // when
        storedProductsService.addUniq(p2);
        // then
        assertThat(storedProducts.get(p2.getName()).getAmount()).isEqualTo( 180.0);
    }

    @Test
    void should_remove_ing_and_stored_product_when_they_have_the_same_amounts() {
        // given
        List<IngredientDto> ings = List.of(
                new IngredientDto("ing1", 100.0, "g")
        );
        // when
        List<IngredientDto> removed = storedProductsService.remove(ings);
        // then
        assertThat(removed).isEmpty();
        assertThat(storedProductsService.getStoredProducts()).isEmpty();
    }

    @Test
    void should_remove_ing_and_subtract_stored_product_when_ings_amount_is_smaller_then_products() {
        // given
        List<IngredientDto> ings = List.of(
                new IngredientDto("ing1", 30.0, "g")
        );
        // when
        List<IngredientDto> removed = storedProductsService.remove(ings);
        // then
        assertThat(removed).isEmpty();
        assertThat(storedProductsService.getStoredProducts().get(p1.getName()).getAmount()).isEqualTo(70.0);
    }

    @Test
    void should_subtract_ing_and_remove_stored_product_when_ings_amount_is_bigger_then_products() {
        // given
        List<IngredientDto> ings = List.of(
                new IngredientDto("ing1", 120.0, "g")
        );
        // when
        List<IngredientDto> removed = storedProductsService.remove(ings);
        // then
        assertThat(removed.get(0).getAmount()).isEqualTo(20.0);
        assertThat(storedProductsService.getStoredProducts()).isEmpty();
    }
}