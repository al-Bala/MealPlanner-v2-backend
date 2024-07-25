package com.mealplannerv2.storage.leftovers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LeftoversGrouperTest {

    LeftoversGrouper storedProductsGrouper;

    @Mock
    LeftoversService storedProductsService;

    private Map<String, LeftoverDto> storedProducts;
    LeftoverDto p1;
    LeftoverDto p2;
    LeftoverDto p3;
    LeftoverDto p4;

    @BeforeEach
    void setUp() {
        storedProductsGrouper = new LeftoversGrouper(storedProductsService);

        p1 = new LeftoverDto("name1", 1.0, "", 1);
        p2 = new LeftoverDto("name2", 2.0, "", 2);
        p3 = new LeftoverDto("name3", 2.0, "", 2);
        p4 = new LeftoverDto("name4", 3.0, "", 3);
    }

    @Test
    void should_return_products_with_1_day_to_spoil_after_opening() {
        // given
        storedProducts = new HashMap<>();
        storedProducts.put(p1.getName(), p1);
        storedProducts.put(p2.getName(), p2);
        // when
        when(storedProductsService.getLeftovers()).thenReturn(storedProducts);
        List<LeftoverDto> productsWithTheFewestDaysToSpoil = storedProductsGrouper.getLeftoversWithTheFewestDaysToSpoil(1);
        // then
        List<LeftoverDto> expectedList = Arrays.asList(p1);
        assertThat(productsWithTheFewestDaysToSpoil).containsAll(expectedList);
    }

    @Test
    void should_return_products_with_2_days_to_spoil_after_opening() {
        // given
        storedProducts = new HashMap<>();
        storedProducts.put(p2.getName(), p2);
        storedProducts.put(p3.getName(), p3);
        storedProducts.put(p4.getName(), p4);
        // when
        when(storedProductsService.getLeftovers()).thenReturn(storedProducts);
        List<LeftoverDto> productsWithTheFewestDaysToSpoil = storedProductsGrouper.getLeftoversWithTheFewestDaysToSpoil(1);
        // then
        List<LeftoverDto> expectedList = Arrays.asList(p2,p3);
        assertThat(productsWithTheFewestDaysToSpoil).containsAll(expectedList);
    }
}