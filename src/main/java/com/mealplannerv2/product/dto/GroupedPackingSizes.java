package com.mealplannerv2.product.dto;

import lombok.Data;

import java.util.List;

@Data
public class GroupedPackingSizes {
    private List<Integer> smallerPackets;
    private List<Integer> biggerPackets;
    private Integer packingSizEqualsAmountInRecipe;

    public GroupedPackingSizes(List<Integer> smallerPackets, List<Integer> biggerPackets) {
        this.smallerPackets = smallerPackets;
        this.biggerPackets = biggerPackets;
        this.packingSizEqualsAmountInRecipe = null;
    }
}
