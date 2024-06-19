package com.mealplannerv2.productstorage.storedleftovers;

import com.mealplannerv2.plangenerator.recipefilter.dto.IngredientDto;
import com.mealplannerv2.product.ProductFacade;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@AllArgsConstructor
@Component
class StoredLeftoversMapper {

    private final ProductFacade productFacade;

    public List<StoredLeftoverDto> mapFromIngredientsDtoToStoredProducts(List<IngredientDto> userIngs){
        return userIngs.stream()
                .map(this::mapFromOneIngDtoToOneStoredProduct)
                .toList();
    }

    private StoredLeftoverDto mapFromOneIngDtoToOneStoredProduct(IngredientDto userIng){
        return new StoredLeftoverDto(
                userIng.getName(),
                userIng.getAmount(),
                userIng.getUnit(),
                productFacade.getMaxDaysAfterOpeningFromDb(userIng.getName()));
    }
}
