package com.mealplannerv2.productstorage.userproducts;

import com.mealplannerv2.plangenerator.recipefilter.dto.IngredientDto;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Log4j2
@Component
public class UserProductsFacade {

    private static final int ONE_DAY = 1;
    private final UserProductsService userProductsService;
//    private final StoredProductsGrouper storedProductsGrouper;
//    private final ProductStorageMapper mapper;

    public Map<String, UserProductDto> getUserProducts(){
        return userProductsService.getStoredProducts();
    }

//    public void addToUserProducts(UserProductDto newProduct){
//        userProductsService.addUniq(newProduct);
//    }

    public void addAllToUserProducts(List<UserProductDto> newProducts){
        userProductsService.addAllUniq(newProducts);
    }

    public void convertAndAddToUserProducts(List<IngredientDto> ingredients) {
        List<UserProductDto> convertedUserLeftovers = convertIntoUserProducts(ingredients);
        addAllToUserProducts(convertedUserLeftovers);
    }

    public List<IngredientDto> subtractUserProductsFromIngsInRecipe(List<IngredientDto> ings){
        return userProductsService.remove(ings);
    }

//    public void updateSpoilDatesForStoredProducts(){
//        userLeftoversService.updateDaysToSpoilAfterOpening();
//    }

    public List<UserProductDto> convertIntoUserProducts(List<IngredientDto> userIngs){
        return mapFromIngredientsDtoToUserProducts(userIngs);
    }

    public List<UserProductDto> mapFromIngredientsDtoToUserProducts(List<IngredientDto> userIngs){
        return userIngs.stream()
                .map(this::mapFromOneIngDtoToOneUserProduct)
                .toList();
    }

    private UserProductDto mapFromOneIngDtoToOneUserProduct(IngredientDto userIng){
        return new UserProductDto(
                userIng.getName(),
                userIng.getAmount(),
                userIng.getUnit());
    }

}
