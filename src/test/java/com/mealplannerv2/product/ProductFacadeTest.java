package com.mealplannerv2.product;

import com.mealplannerv2.plangenerator.recipefilter.dto.IngredientDto;
import com.mealplannerv2.plangenerator.recipefilter.dto.RecipeDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductFacadeTest {

    ProductFacade productFacade;

    @Mock
    ProductRepository productRepository;
    @Mock
    PackingChooser packingChooser;

    @BeforeEach
    void setUp() {
        productFacade = new ProductFacade(productRepository, packingChooser);
    }

//    @Test
//    void choosePacketForEachIngredient() {
//        RecipeDto recipeDto = RecipeDto.builder()
//                .ingredients(List.of(
//                        new IngredientDto("ing1", 150.0, "u"),
//                        new IngredientDto("ing2", 350.0, "u")))
//                .build();
//
//        when(packingChooser.dividePacketsIntoSmallerAndLargerThanNeededIng())
//
//    }
}