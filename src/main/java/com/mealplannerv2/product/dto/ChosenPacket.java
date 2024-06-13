package com.mealplannerv2.product.dto;

import com.mealplannerv2.plangenerator.recipefilter.dto.IngredientDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ChosenPacket {
    private IngredientDto ingredientDto;
    private double packingSize;
//    String packetUnit;
    private int packetsNumber;
    private double leftovers;

    public ChosenPacket() {
    }
}
