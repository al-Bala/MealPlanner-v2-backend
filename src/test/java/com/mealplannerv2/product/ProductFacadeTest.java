package com.mealplannerv2.product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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