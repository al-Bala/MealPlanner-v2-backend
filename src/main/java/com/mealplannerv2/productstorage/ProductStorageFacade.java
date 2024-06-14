package com.mealplannerv2.productstorage;

import com.mealplannerv2.plangenerator.recipefilter.dto.IngredientDto;
import com.mealplannerv2.productstorage.dto.StoredProductDto;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Log4j2
@Component
public class ProductStorageFacade {

    private static final int ONE_DAY = 1;
    private final StoredProductsService storedProductsService;
    private final StoredProductsGrouper storedProductsGrouper;
    private final ProductStorageMapper mapper;

    public Map<String, StoredProductDto> getStoredProducts(){
        return storedProductsService.getStoredProducts();
    }

    public void addToStoredProducts(StoredProductDto newProduct){
        storedProductsService.addUniq(newProduct);
    }

    public void addAllToStoredProducts(List<StoredProductDto> newProducts){
        storedProductsService.addAllUniq(newProducts);
    }

    public void convertAndAddToStoredProducts(List<IngredientDto> ingredients) {
        List<StoredProductDto> convertedProductsToStore = convertIntoStoredProducts(ingredients);
        addAllToStoredProducts(convertedProductsToStore);
    }

    public List<IngredientDto> subtractStoredProductsFromIngsInRecipe(List<IngredientDto> ings){
        return storedProductsService.remove(ings);
    }

    public void updateSpoilDatesForStoredProducts(){
        storedProductsService.updateDaysToSpoilAfterOpening();
    }

    public List<StoredProductDto> convertIntoStoredProducts(List<IngredientDto> userIngs){
        return mapper.mapFromIngredientsDtoToStoredProducts(userIngs);
    }

    public List<StoredProductDto> getProductsWhichMustBeUsedFirstly() {
        if (getStoredProducts().isEmpty()) {
            log.warn("StoredProducts is empty.");
            return null;
        } else {
            return storedProductsGrouper.getProductsWithTheFewestDaysToSpoil(ONE_DAY);
        }
    }
}
