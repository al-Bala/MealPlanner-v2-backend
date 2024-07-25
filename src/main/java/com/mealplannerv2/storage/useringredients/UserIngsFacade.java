package com.mealplannerv2.storage.useringredients;

import com.mealplannerv2.storage.IngredientDto;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Log4j2
@Component
public class UserIngsFacade {

    private static final int ONE_DAY = 1;
    private final UserIngsService userIngsService;
//    private final StoredProductsGrouper storedProductsGrouper;
//    private final ProductStorageMapper mapper;

    public Map<String, IngredientDto> getUserIngs(){
        return userIngsService.getUserIngs();
    }

//    public void addToUserProducts(UserProductDto newProduct){
//        userProductsService.addUniq(newProduct);
//    }

    public void addAllToUserIngs(List<IngredientDto> newIngs){
        userIngsService.addAllUniq(newIngs);
    }

//    public void addToUserProducts(List<IngredientDto2> ingredients) {
////        List<UserProductDto> convertedUserLeftovers = convertIntoUserProducts(ingredients);
////        addAllToUserProducts(convertedUserLeftovers);
//    }

    public List<IngredientDto> removeUsedUserIngs(List<IngredientDto> ings){
        return userIngsService.remove(ings);
    }

//    public void updateSpoilDatesForStoredProducts(){
//        userLeftoversService.updateDaysToSpoilAfterOpening();
//    }

//    public List<UserProductDto> convertIntoUserProducts(List<IngredientDto2> userIngs){
//        return mapFromIngredientsDtoToUserProducts(userIngs);
//    }
//
//    public List<UserProductDto> mapFromIngredientsDtoToUserProducts(List<IngredientDto2> userIngs){
//        return userIngs.stream()
//                .map(this::mapFromOneIngDtoToOneUserProduct)
//                .toList();
//    }
//
//    private UserProductDto mapFromOneIngDtoToOneUserProduct(IngredientDto2 userIng){
//        return new UserProductDto(
//                userIng.getName(),
//                userIng.getAmount(),
//                userIng.getUnit());
//    }

}
