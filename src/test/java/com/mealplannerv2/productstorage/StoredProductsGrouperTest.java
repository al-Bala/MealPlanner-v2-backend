package com.mealplannerv2.productstorage;

import com.mealplannerv2.productstorage.dto.StoredProductDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StoredProductsGrouperTest {

    StoredProductsGrouper storedProductsGrouper;

    @Mock
    StoredProductsService storedProductsService;

    private Set<StoredProductDto> storedProducts;
    StoredProductDto p1;
    StoredProductDto p2;
    StoredProductDto p3;
    StoredProductDto p4;

    @BeforeEach
    void setUp() {
        storedProductsGrouper = new StoredProductsGrouper(storedProductsService);

        p1 = new StoredProductDto("name1","", 1);
        p2 = new StoredProductDto("name2","", 2);
        p3 = new StoredProductDto("name3", "", 2);
        p4 = new StoredProductDto("name4", "", 3);
    }

    @Test
    void should_return_products_with_1_day_to_spoil_after_opening() {
        // given
        storedProducts = new HashSet<>(List.of(p1, p2));
        // when
        when(storedProductsService.getStoredProducts()).thenReturn(storedProducts);
        List<StoredProductDto> productsWithTheFewestDaysToSpoil = storedProductsGrouper.getProductsWithTheFewestDaysToSpoil(1);
        // then
        List<StoredProductDto> expectedList = Arrays.asList(p1);
        assertThat(productsWithTheFewestDaysToSpoil).containsAll(expectedList);
    }

    @Test
    void should_return_products_with_2_days_to_spoil_after_opening() {
        // given
        storedProducts = new HashSet<>(List.of(p2, p3, p4));
        // when
        when(storedProductsService.getStoredProducts()).thenReturn(storedProducts);
        List<StoredProductDto> productsWithTheFewestDaysToSpoil = storedProductsGrouper.getProductsWithTheFewestDaysToSpoil(1);
        // then
        List<StoredProductDto> expectedList = Arrays.asList(p2,p3);
        assertThat(productsWithTheFewestDaysToSpoil).containsAll(expectedList);
    }
}