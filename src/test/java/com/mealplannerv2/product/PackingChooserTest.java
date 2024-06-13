package com.mealplannerv2.product;

import com.mealplannerv2.plangenerator.recipefilter.dto.IngredientDto;
import com.mealplannerv2.product.dto.GroupedPackingSizes;
import com.mealplannerv2.product.dto.ChosenPacket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PackingChooserTest {

    PackingChooser packingChooser;

    @Mock
    ProductRepository productRepository;

    Product product;

    @BeforeEach
    void setUp() {
        packingChooser = new PackingChooser(productRepository);
        product = Product.builder()
                .name("ing")
                .packing_sizes(List.of(80, 120, 200))
                .build();
    }

    @Test
    void should_add_packingSizes_to_proper_lists() {
        // given
        IngredientDto ing = new IngredientDto("ing", 100.0, "u");
        // when
        when(productRepository.getProductByName("ing")).thenReturn(product);
        GroupedPackingSizes groupedPackingSizes = packingChooser.dividePacketsIntoSmallerAndLargerThanNeededIng(ing);
        // then
        assertThat(groupedPackingSizes.getSmallerPackets()).isEqualTo(List.of(80));
        assertThat(groupedPackingSizes.getBiggerPackets()).isEqualTo(List.of(120,200));
    }

    @Test
    void should_set_packingSizeEqualsAmountInRecipe() {
        // given
        IngredientDto ing = new IngredientDto("ing", 120.0, "u");
        // when
        when(productRepository.getProductByName("ing")).thenReturn(product);
        GroupedPackingSizes groupedPackingSizes = packingChooser.dividePacketsIntoSmallerAndLargerThanNeededIng(ing);
        // then
        assertThat(groupedPackingSizes.getPackingSizEqualsAmountInRecipe()).isEqualTo(120);
    }

    @Test
    void should_choose_450_packetSize_for_300_from_200_450() {
        // given
        IngredientDto ing = new IngredientDto("ing", 300.0, "u");
        GroupedPackingSizes groupedPackingSizes = new GroupedPackingSizes(List.of(200), List.of(450));
        // when
        ChosenPacket chosenPacket = packingChooser.choosePacketForWhichTheLeastProductIsWasted(ing, groupedPackingSizes);
        // then
        assertThat(chosenPacket.getPackingSize()).isEqualTo(450);
    }

    @Test
    void should_choose_160_packetSize_for_150_from_80_160_180_200() {
        // given
        IngredientDto ing = new IngredientDto("ing", 150.0, "u");
        GroupedPackingSizes groupedPackingSizes = new GroupedPackingSizes(List.of(80), List.of(160,180,200));
        // when
        ChosenPacket chosenPacket = packingChooser.choosePacketForWhichTheLeastProductIsWasted(ing, groupedPackingSizes);
        // then
        assertThat(chosenPacket.getPackingSize()).isEqualTo(160);
    }

    @Test
    void should_choose_350_packetSize_for_280_from_100_350() {
        // given
        IngredientDto ing = new IngredientDto("ing", 280.0, "u");
        GroupedPackingSizes groupedPackingSizes = new GroupedPackingSizes(List.of(100), List.of(350));
        // when
        ChosenPacket chosenPacket = packingChooser.choosePacketForWhichTheLeastProductIsWasted(ing, groupedPackingSizes);
        // then
        assertThat(chosenPacket.getPackingSize()).isEqualTo(350);
    }

}