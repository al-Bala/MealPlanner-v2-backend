package com.mealplannerv2.product;

import com.mealplannerv2.plangenerator.recipefilter.dto.IngredientDto;
import com.mealplannerv2.plangenerator.recipefilter.dto.RecipeDto;
import com.mealplannerv2.product.dto.GroupedPackingSizes;
import com.mealplannerv2.product.dto.Result;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Log4j2
@Component
public class ProductFacade {

    private final ProductRepository productRepository;
    private final PackingChooser packingChooser;

    public List<Result> choosePacketForEachIngredient(RecipeDto recipeDto) {
        List<Result> results = new ArrayList<>();
        for(IngredientDto ing: recipeDto.getIngredients()){
            GroupedPackingSizes groupedPackingSizes = packingChooser.dividePacketsIntoSmallerAndLargerThanNeededIng(ing);
            Integer packingSizEqualsAmountInRecipe = groupedPackingSizes.getPackingSizEqualsAmountInRecipe();
            if(packingSizEqualsAmountInRecipe != null){
                Result equalResult = Result.builder()
                        .ingredientDto(ing)
                        .chosenPacketSize(packingSizEqualsAmountInRecipe)
                        .packetsNumber(1)
                        .leftovers(0)
                        .build();
                results.add(equalResult);
                break;
            }
            Result result = packingChooser.choosePacketForWhichTheLeastProductIsWasted(ing, groupedPackingSizes);
            results.add(result);
        }
        return results;
    }

    public Integer getMaxDaysAfterOpeningFromDb(String userProductName){
        Product productDb = productRepository.getProductByName(userProductName);
        return productDb.maxDaysAfterOpening();
    }

}
