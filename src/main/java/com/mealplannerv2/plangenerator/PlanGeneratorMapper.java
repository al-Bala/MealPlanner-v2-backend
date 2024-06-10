package com.mealplannerv2.plangenerator;

import com.mealplannerv2.plangenerator.infrastructure.controller.dto.ProductFromUser;
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

    public List<UserProduct> mapFromProductsFromUserToUserProducts(List<ProductFromUser> productsFromUser) {
        List<UserProduct> userProducts = new ArrayList<>();
        for(ProductFromUser p : productsFromUser){
            if(p.mainAmountUnit() == null){
                userProducts.add(new UserProduct(p.name(), p.anyAmountUnit().amount(), p.anyAmountUnit().unit()));
            } else {
                userProducts.add(new UserProduct(p.name(), p.mainAmountUnit().amount(), p.mainAmountUnit().unit()));
            }
        }
        return userProducts;
    }

}
