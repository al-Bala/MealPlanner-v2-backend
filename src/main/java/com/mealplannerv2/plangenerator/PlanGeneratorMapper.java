package com.mealplannerv2.plangenerator;

import com.mealplannerv2.plangenerator.infrastructure.controller.dto.ProductFromUser;
import com.mealplannerv2.plangenerator.recipefilter.dto.IngredientDto;
import com.mealplannerv2.plangenerator.recipefilter.model.Ingredient;
import com.mealplannerv2.productstorage.dto.StoredProductDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
class PlanGeneratorMapper {

    public static List<Ingredient> mapFromStoredProductsToIngredients(List<StoredProductDto> productsToUseFirstly){
        return productsToUseFirstly.stream()
                .map(product -> new Ingredient(product.getName(), product.getAmount(), product.getUnit()))
                .toList();
    }

    public List<IngredientDto> mapFromProductsFromUserToIngredientDto(List<ProductFromUser> productsFromUser) {
        List<IngredientDto> userIngs = new ArrayList<>();
        for(ProductFromUser p : productsFromUser){
            if(p.mainAmountUnit() == null){
                userIngs.add(new IngredientDto(p.name(), p.anyAmountUnit().amount(), p.anyAmountUnit().unit()));
            } else {
                userIngs.add(new IngredientDto(p.name(), p.mainAmountUnit().amount(), p.mainAmountUnit().unit()));
            }
        }
        return userIngs;
    }

}
