package com.mealplannerv2.product;

import com.mealplannerv2.plangenerator.recipefilter.dto.IngredientDto;
import com.mealplannerv2.product.dto.GroupedPackingSizes;
import com.mealplannerv2.product.dto.Left;
import com.mealplannerv2.product.dto.Result;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Component
class PackingChooser {

    static final int ONE_PACKET = 1;
    private final ProductRepository productRepository;

    public GroupedPackingSizes dividePacketsIntoSmallerAndLargerThanNeededIng(IngredientDto ing) {
        Product productByName = productRepository.getProductByName(ing.getName());
        List<Integer> listOfPackingSizes = productByName.packingSizes();
        double ingAmount = ing.getAmount();
        GroupedPackingSizes groupedPackingSizes = new GroupedPackingSizes(new ArrayList<>(), new ArrayList<>());

        for(int packingSize: listOfPackingSizes){
            if (packingSize > ingAmount) {
                List<Integer> biggerPackets = groupedPackingSizes.getBiggerPackets();
                biggerPackets.add(packingSize);
            } else if (packingSize < ingAmount) {
                List<Integer> smallerPackets = groupedPackingSizes.getSmallerPackets();
                smallerPackets.add(packingSize);
            } else {
                groupedPackingSizes.setPackingSizEqualsAmountInRecipe(packingSize);
            }
        }
        return null;
    }

    public Result choosePacketForWhichTheLeastProductIsWasted(IngredientDto ing, GroupedPackingSizes groupedPackingSizes) {
        Result big = getOnePacketWithTheLeastLeftovers(ing, groupedPackingSizes.getBiggerPackets());
        Result small = getOnePacketWithTheLeastLeftovers(ing, groupedPackingSizes.getSmallerPackets());

        double factorBigger = big.getPacketsNumber()* big.getLeftovers();
        double factorSmaller = small.getPacketsNumber() * small.getLeftovers();

        if(factorSmaller < (0.7 * factorBigger)){
            return small;
        } else {
            return big;
        }
    }

    private Result getOnePacketWithTheLeastLeftovers(IngredientDto ing, List<Integer> packetsSizes) {
        Result finalResult = new Result();
        for (Integer packingSize : packetsSizes) {
            Left left = countLeftovers(ing.getAmount(), packingSize, ONE_PACKET);

            if(finalResult.getLeftovers() > left.leftovers()){
                finalResult = Result.builder()
                        .ingredientDto(ing)
                        .chosenPacketSize(packingSize)
//                        .packetUnit()
                        .packetsNumber(left.packetsNumber())
                        .leftovers(left.leftovers())
                        .build();;
            }
        }
        return finalResult;
    }

    private Left countLeftovers(double amount, int packingSize, int packetsNumber){
        double leftovers = amount - packingSize;
        if(leftovers <= 0){
            return new Left(Math.abs(leftovers), packetsNumber);
        }
        return countLeftovers(leftovers, packingSize, ++packetsNumber);
    }
}
