package com.mealplannerv2.product;

import lombok.Data;

import java.util.List;

@Data
public class GroupedPackingSizes {

    private static int ONE_PACKET = 1;

    private List<Integer> smallerPackets;
    private List<Integer> biggerPackets;
    private Double packingSizEqualOrByWeight;
    private Integer packetsNumber;

    public GroupedPackingSizes(List<Integer> smallerPackets, List<Integer> biggerPackets) {
        this.smallerPackets = smallerPackets;
        this.biggerPackets = biggerPackets;
        this.packingSizEqualOrByWeight = null;
        this.packetsNumber = null;
    }
}
