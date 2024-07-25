package com.mealplannerv2.storage.leftovers;

import com.mealplannerv2.product.ProductFacade;
import com.mealplannerv2.storage.IngredientDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@AllArgsConstructor
@Component
class LeftoversMapper {

    private final ProductFacade productFacade;

    public List<LeftoverDto> mapFromIngredientsDtoToLeftovers(List<IngredientDto> ingsDto){
        return ingsDto.stream()
                .map(this::mapFromIngDtoToLeftover)
                .toList();
    }

    private LeftoverDto mapFromIngDtoToLeftover(IngredientDto ingDto){
        return new LeftoverDto(
                ingDto.getName(),
                ingDto.getAmount(),
                ingDto.getUnit(),
                productFacade.getMaxDaysAfterOpeningFromDb(ingDto.getName()));
    }
}
