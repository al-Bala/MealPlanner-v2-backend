package com.mealplannerv2.grocerylist;

import com.mealplannerv2.storage.IngredientDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@AllArgsConstructor
public class GroceryListFacade {

    private final GroceryListService groceryListService;

    public Set<IngredientDto> getGroceryList(){
        return groceryListService.getGroceryList();
    }

    public void addToGroceryList(IngredientDto ingToBuy){
        groceryListService.addUniq(ingToBuy);
    }

    public void addAllToGroceryList(List<IngredientDto> ingsToBuy){
        groceryListService.addAllUniq(ingsToBuy);
    }

}
