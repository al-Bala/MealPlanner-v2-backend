package com.mealplannerv2.plangenerator.grocerylist;

import com.mealplannerv2.plangenerator.recipefilter.dto.IngredientDto;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
class GroceryListService {

    private static final Set<IngredientDto> groceryList = new HashSet<>();

    public Set<IngredientDto> getGroceryList(){
        return groceryList;
    }

    public void addAllUniq(List<IngredientDto> ingsToBuy){
        for(IngredientDto product: ingsToBuy){
            addUniq(product);
        }
    }

    public void addUniq(IngredientDto ingToBuy){
        if (groceryList.isEmpty()) {
            groceryList.add(ingToBuy);
        } else {
            int i = 0;
            for (IngredientDto ingInList : groceryList) {
                if (ingInList.equals(ingToBuy)) {
                    double currentAmount = ingInList.getAmount();
                    double newAmount = currentAmount + ingToBuy.getAmount();
                    ingInList.setAmount(newAmount);
                    break;
                }
                i++;
            }
            if (i == groceryList.size()) {
                groceryList.add(ingToBuy);
            }
        }
    }
}
