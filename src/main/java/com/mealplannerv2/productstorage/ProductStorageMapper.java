package com.mealplannerv2.productstorage;

import com.mealplannerv2.plangenerator.recipefilter.dto.IngredientDto;
import com.mealplannerv2.product.ProductFacade;
import com.mealplannerv2.productstorage.dto.StoredProductDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@AllArgsConstructor
@Component
class ProductStorageMapper {

    private final ProductFacade productFacade;

    public List<StoredProductDto> mapFromIngredientsDtoToStoredProducts(List<IngredientDto> userIngs){
        return userIngs.stream()
                .map(this::mapFromOneIngDtoToOneStoredProduct)
                .toList();
    }

    private StoredProductDto mapFromOneIngDtoToOneStoredProduct(IngredientDto userIng){
        return StoredProductDto.builder()
                .name(userIng.getName())
                .amountToUse(userIng.getAmount())
                .unit(userIng.getUnit())
                // wartość ustaawiana na podstwie danych z bazy o produkcie
                .daysToSpoilAfterOpening(productFacade.getMaxDaysAfterOpeningFromDb(userIng.getName()))
                .build();
    }
}
