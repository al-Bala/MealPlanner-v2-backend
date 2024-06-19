package com.mealplannerv2.plangenerator;

import com.mealplannerv2.plangenerator.infrastructure.controller.dto.ProductFromUser;
import com.mealplannerv2.plangenerator.recipefilter.dto.IngredientDto;
import com.mealplannerv2.plangenerator.recipefilter.model.Ingredient;
import com.mealplannerv2.product.Product;
import com.mealplannerv2.product.ProductRepository;
import com.mealplannerv2.product.unitreader.UnitsReaderImpl;
import com.mealplannerv2.product.unitreader.dto.Units;
import com.mealplannerv2.productstorage.storedleftovers.StoredLeftoverDto;
import com.mealplannerv2.productstorage.userproducts.UserProductDto;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Log4j2
@AllArgsConstructor
@Component
class PlanGeneratorMapper {

    private final ProductRepository productRepository;
    private final UnitsReaderImpl unitsReaderImpl;

    public List<IngredientDto> mapFromProductsFromUserToIngredientDto(List<ProductFromUser> productsFromUser) {
        List<IngredientDto> userIngs = new ArrayList<>();
        for(ProductFromUser p : productsFromUser){
            if(p.mainAmountUnit() == null){
                Product productDb = productRepository.getProductByName(p.name());
                if(!p.anyAmountUnit().unit().equals(productDb.main_unit())){
                    Units f = unitsReaderImpl.getUnits();
                    Double convertedUnit = f.convertUnit(p.anyAmountUnit().amount(), p.anyAmountUnit().unit(), productDb.main_unit());
                    userIngs.add(new IngredientDto(p.name(), convertedUnit, productDb.main_unit()));
                } else {
                    userIngs.add(new IngredientDto(p.name(), p.anyAmountUnit().amount(), p.anyAmountUnit().unit()));
                }
            } else {
                userIngs.add(new IngredientDto(p.name(), p.mainAmountUnit().amount(), p.mainAmountUnit().unit()));
            }
        }
        return userIngs;
    }

    public static List<Ingredient> mapFromStoredProductsToIngredients(List<StoredLeftoverDto> productsToUseFirstly){
        return productsToUseFirstly.stream()
                .map(product -> new Ingredient(product.getName(), product.getAmount(), product.getUnit()))
                .toList();
    }

    public static List<Ingredient> mapFromUserProductsToIngredients(Collection<UserProductDto> userProducts){
        return userProducts.stream()
                .map(product -> new Ingredient(product.getName(), product.getAmount(), product.getUnit()))
                .toList();
    }
}
