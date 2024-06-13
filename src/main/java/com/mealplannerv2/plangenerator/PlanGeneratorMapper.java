package com.mealplannerv2.plangenerator;

import com.mealplannerv2.plangenerator.infrastructure.controller.dto.ProductFromUser;
import com.mealplannerv2.plangenerator.recipefilter.dto.IngredientDto;
import com.mealplannerv2.plangenerator.recipefilter.model.Ingredient;
import com.mealplannerv2.product.Product;
import com.mealplannerv2.product.ProductRepository;
import com.mealplannerv2.product.unit.Units;
import com.mealplannerv2.product.unit.UnitsReader;
import com.mealplannerv2.productstorage.dto.StoredProductDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Component
class PlanGeneratorMapper {

    private final ProductRepository productRepository;
    private final UnitsReader unitsReader;

    public static List<Ingredient> mapFromStoredProductsToIngredients(List<StoredProductDto> productsToUseFirstly){
        return productsToUseFirstly.stream()
                .map(product -> new Ingredient(product.getName(), product.getAmount(), product.getUnit()))
                .toList();
    }

    public List<IngredientDto> mapFromProductsFromUserToIngredientDto(List<ProductFromUser> productsFromUser) {
        List<IngredientDto> userIngs = new ArrayList<>();
        for(ProductFromUser p : productsFromUser){
            if(p.mainAmountUnit() == null){
                Product productDb = productRepository.getProductByName(p.name());
                if(!p.anyAmountUnit().unit().equals(productDb.main_unit())){
                    Units f = unitsReader.getUnits();
                    Double convertedUnit = f.convertUnit(p.anyAmountUnit().amount(), p.anyAmountUnit().unit(), productDb.main_unit());
                    userIngs.add(new IngredientDto(p.name(), convertedUnit, productDb.main_unit()));
                }
                userIngs.add(new IngredientDto(p.name(), p.anyAmountUnit().amount(), p.anyAmountUnit().unit()));
            } else {
                userIngs.add(new IngredientDto(p.name(), p.mainAmountUnit().amount(), p.mainAmountUnit().unit()));
            }
        }
        return userIngs;
    }

}
