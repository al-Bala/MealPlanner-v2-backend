package com.mealplannerv2.product.productkeeper;

import com.mealplannerv2.product.ProductGrouper;
import com.mealplannerv2.plangenerator.recipefilter.dto.IngredientDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.*;

@Log4j2
@Component
public class ProductKeeperFacade {

    private static final Comparator<IngredientDto> comparator = new IngredientDtoComparator();
    public static Set<IngredientDto> productsInUse = new TreeSet<>(comparator);

    public static void addUniq(IngredientDto newProduct){
        if (productsInUse.isEmpty()) {
            productsInUse.add(newProduct);
        } else {
            int i = 0;
            for (IngredientDto keptProduct : productsInUse) {
                if (keptProduct.equals(newProduct)) {
                    double currentAmount = keptProduct.getAmount();
                    double newAmount = currentAmount + newProduct.getAmount();
                    keptProduct.setAmount(newAmount);
                    break;
                }
                i++;
            }
            if (i == productsInUse.size()) {
                productsInUse.add(newProduct);
            }
        }
    }

    public static void addAllUniq(List<IngredientDto> newProducts){
        for(IngredientDto ing: newProducts){
            addUniq(ing);
        }
    }

    public static List<IngredientDto> getAllWithGivenDaysNumber(long waitingLevel) {
        List<IngredientDto> allWithGivenDaysNumber = ProductGrouper.getAllWithGivenWaitingLevel(waitingLevel);
        if(allWithGivenDaysNumber == null){
            log.warn("Set: productsInUse is empty.");
            return Collections.emptyList();
        } else {
            return allWithGivenDaysNumber;
        }
    }
}
