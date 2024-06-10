package com.mealplannerv2.product.dto;

import com.mealplannerv2.plangenerator.recipefilter.dto.IngredientDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Result {
    private IngredientDto ingredientDto;
    private int chosenPacketSize;
//    String packetUnit;
    private int packetsNumber;
    private double leftovers;

    public Result() {
    }
}
