package com.mealplannerv2.product;

import com.mealplannerv2.plangenerator.recipefilter.dto.IngredientDto;
import com.mealplannerv2.product.dto.GroupedPackingSizes;
import com.mealplannerv2.product.dto.PacketsNrWithLeftovers;
import com.mealplannerv2.product.dto.ChosenPacket;
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
                break;
            }
        }
        return groupedPackingSizes;
    }

    public ChosenPacket choosePacketForWhichTheLeastProductIsWasted(IngredientDto ing, GroupedPackingSizes groupedPackingSizes) {
        ChosenPacket biggerPack = getOnePacketWithTheLeastLeftovers(ing, groupedPackingSizes.getBiggerPackets());
        ChosenPacket smallerPack = getOnePacketWithTheLeastLeftovers(ing, groupedPackingSizes.getSmallerPackets());

        double factorBigger = biggerPack.getPacketsNumber()* biggerPack.getLeftovers();
        double factorSmaller = smallerPack.getPacketsNumber() * smallerPack.getLeftovers();

        if(factorSmaller < (0.7 * factorBigger)){
            return smallerPack;
        } else {
            return biggerPack;
        }
    }

    private ChosenPacket getOnePacketWithTheLeastLeftovers(IngredientDto ing, List<Integer> packingSizes) {
        ChosenPacket finalChosenPacket = ChosenPacket.builder().leftovers(-1).build();
        for (Integer packingSize : packingSizes) {
            PacketsNrWithLeftovers packetsNrWithLeftovers = countPacketsNrAndLeftovers(ing.getAmount(), packingSize, ONE_PACKET);

            if(finalChosenPacket.getLeftovers() > packetsNrWithLeftovers.leftovers() || finalChosenPacket.getLeftovers() == -1){
                finalChosenPacket = ChosenPacket.builder()
                        .ingredientDto(ing)
                        .packingSize(packingSize)
//                        .packetUnit()
                        .packetsNumber(packetsNrWithLeftovers.packetsNumber())
                        .leftovers(packetsNrWithLeftovers.leftovers())
                        .build();;
            }
        }
        return finalChosenPacket;
    }

    private PacketsNrWithLeftovers countPacketsNrAndLeftovers(double amount, int packingSize, int packetsNumber){
        double leftovers = amount - packingSize;
        if(leftovers <= 0){
            return new PacketsNrWithLeftovers(packetsNumber, Math.abs(leftovers));
        }
        return countPacketsNrAndLeftovers(leftovers, packingSize, ++packetsNumber);
    }
}
