package com.mealplannerv2.plangenerator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mealplannerv2.plangenerator.infrastructure.controller.dto.AmountUnit;
import com.mealplannerv2.plangenerator.infrastructure.controller.dto.ProductFromUser;
import com.mealplannerv2.plangenerator.recipefilter.dto.IngredientDto;
import com.mealplannerv2.product.Product;
import com.mealplannerv2.product.ProductRepository;
import com.mealplannerv2.product.unitreader.dto.Units;
import com.mealplannerv2.product.unitreader.UnitsReaderImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlanGeneratorMapperTest {

    PlanGeneratorMapper planGeneratorMapper;
    @Mock
    ProductRepository productRepository;
    @Mock
    UnitsReaderImpl unitsReader;

    @BeforeEach
    void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();
        unitsReader = new UnitsReaderImpl(objectMapper) {
            @Override
            public Units getUnits() {
                try {
                    File file = new File("src/test/resources/units-test.json");
                    return objectMapper.readValue(file, Units.class);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        };
        planGeneratorMapper = new PlanGeneratorMapper(productRepository, unitsReader);
    }

    @Test
    void should_return_the_same_amount_and_unit_when_mainAmountUnit_is_null_and_units_are_equal() {
        // given
        List<ProductFromUser> productFromUsers = List.of(
                new ProductFromUser(
                        "product",
                        new AmountUnit(2.0, "szt."),
                        null));
        // when
        when(productRepository.getProductByName("product")).thenReturn(
                Product.builder().main_unit("szt.").build()
        );
        List<IngredientDto> ings = planGeneratorMapper.mapFromProductsFromUserToIngredientDto(productFromUsers);
        // then
        assertThat(ings.get(0).getName()).isEqualTo("product");
        assertThat(ings.get(0).getAmount()).isEqualTo(2.0);
        assertThat(ings.get(0).getUnit()).isEqualTo("szt.");
    }

    @Test
    void should_convert_to_main_unit_when_mainAmountUnit_is_null_and_units_not_equal() {
        // given
        List<ProductFromUser> productFromUsers = List.of(
                new ProductFromUser(
                        "product",
                        new AmountUnit(1.0, "kg"),
                        null));
        // when
        when(productRepository.getProductByName("product")).thenReturn(
                Product.builder().main_unit("g").build()
        );
        List<IngredientDto> ings = planGeneratorMapper.mapFromProductsFromUserToIngredientDto(productFromUsers);
        // then
        assertThat(ings.get(0).getName()).isEqualTo("product");
        assertThat(ings.get(0).getAmount()).isEqualTo(1000.0);
        assertThat(ings.get(0).getUnit()).isEqualTo("g");
    }

    @Test
    void should_convert_to_main_unit_based_on_set_mainAmountUnit() {
        // given
        List<ProductFromUser> productFromUsers = List.of(
                new ProductFromUser(
                        "product",
                        new AmountUnit(1.0, "szt."),
                        new AmountUnit(80.0, "g")));
        // when
        List<IngredientDto> ings = planGeneratorMapper.mapFromProductsFromUserToIngredientDto(productFromUsers);
        // then
        assertThat(ings.get(0).getName()).isEqualTo("product");
        assertThat(ings.get(0).getAmount()).isEqualTo(80.0);
        assertThat(ings.get(0).getUnit()).isEqualTo("g");
    }
}