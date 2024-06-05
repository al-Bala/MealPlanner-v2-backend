package com.mealplannerv2.productstorage;

import com.mealplannerv2.plangenerator.recipefilter.dto.UserProduct;
import com.mealplannerv2.productstorage.dto.StoredProductDto;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Log4j2
@Component
public class ProductStorageFacade {

    private final StoredProductsService storedProductsService;
    private final StoredProductsGrouper storedProductsGrouper;

    public Set<StoredProductDto> getStoredProducts(){
        return storedProductsService.getStoredProducts();
    }

    public void addAllToStoredProducts(List<StoredProductDto> newProducts){
        storedProductsService.addAllUniq(newProducts);
    }

    public void addToStoredProducts(StoredProductDto newProduct){
        storedProductsService.addUniq(newProduct);
    }

    public void updateSpoilDatesForStoredProducts(){
        storedProductsService.updateDaysToSpoilAfterOpening();
    }

    public List<StoredProductDto> createNewProducts(List<UserProduct> ingredients){
        return storedProductsService.createNewForStoredProducts(ingredients);
    }

    public List<StoredProductDto> getProductsWhichMustBeUsedFirstly() {
        if (getStoredProducts().isEmpty()) {
            log.warn("StoredProducts is empty.");
            return null;
        } else {
            return storedProductsGrouper.getProductsWithTheFewestDaysToSpoil(1);
        }
    }
}
