package com.mealplannerv2.productstorage.storedleftovers;

import com.mealplannerv2.plangenerator.recipefilter.dto.IngredientDto;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Log4j2
@Component
public class StoredLeftoversFacade {

    private static final int ONE_DAY = 1;
    private final StoredLeftoversService storedLeftoversService;
    private final StoredLeftoversGrouper storedLeftoversGrouper;
    private final StoredLeftoversMapper mapper;

    public Map<String, StoredLeftoverDto> getStoredProducts(){
        return storedLeftoversService.getStoredProducts();
    }

//    public void addToStoredProducts(StoredProductDto2 newProduct){
//        storedProductsService.addUniq(newProduct);
//    }

    public void addAllToStoredProducts(List<StoredLeftoverDto> newProducts){
        storedLeftoversService.addAllUniq(newProducts);
    }

    public void convertAndAddToStoredProducts(List<IngredientDto> ingredients) {
        List<StoredLeftoverDto> convertedProductsToStore = convertIntoStoredProducts(ingredients);
        addAllToStoredProducts(convertedProductsToStore);
    }

    public List<IngredientDto> updateStoredLeftovers(List<IngredientDto> ings){
        return storedLeftoversService.remove(ings);
    }

    public void updateSpoilDatesForStoredProducts(){
        storedLeftoversService.updateDaysToSpoilAfterOpening();
    }

    public List<StoredLeftoverDto> convertIntoStoredProducts(List<IngredientDto> userIngs){
        return mapper.mapFromIngredientsDtoToStoredProducts(userIngs);
    }

    public List<StoredLeftoverDto> getProductsWhichMustBeUsedFirstly() {
        if (getStoredProducts().isEmpty()) {
            log.warn("StoredProducts is empty.");
            return null;
        } else {
            return storedLeftoversGrouper.getProductsWithTheFewestDaysToSpoil(ONE_DAY);
        }
    }
}
