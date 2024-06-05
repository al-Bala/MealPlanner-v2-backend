package com.mealplannerv2.plangenerator;

import com.mealplannerv2.plangenerator.recipefilter.model.Ingredient;
import com.mealplannerv2.productstorage.dto.StoredProductDto;

import java.util.List;

public class PlanGeneratorMapper {

    public static List<Ingredient> mapFromStoredProductsToIngredients(List<StoredProductDto> productsToUseFirstly){
        return productsToUseFirstly.stream()
                .map(product -> new Ingredient(product.getName(), product.getAmount(), product.getUnit()))
                .toList();
    }

}
