package com.mealplannerv2.storage;

import com.mealplannerv2.grocerylist.GroceryListFacade;
import com.mealplannerv2.recipe.model.Ingredient;
import com.mealplannerv2.storage.leftovers.LeftoverDto;
import com.mealplannerv2.storage.leftovers.LeftoversFacade;
import com.mealplannerv2.storage.useringredients.UserIngsFacade;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class StorageFacade {

    private final UserIngsFacade userIngsFacade;
    private final LeftoversFacade leftoversFacade;
    private final GroceryListFacade groceryListFacade;

    public List<Ingredient> getIngsToUseFirstly(){
        List<Ingredient> ingredients;
        if(!userIngsFacade.getUserIngs().isEmpty()){
            List<IngredientDto> userIngs = userIngsFacade.getUserIngs().values().stream().toList();
            ingredients = StorageMapper.mapFromIngredientsDtoToIngredients(userIngs);
        } else {
            // TODO: empty leftovers
            // wybranie produktów z 1 grupy lub zwrócenie null
            List<LeftoverDto> leftoversWhichMustBeUsedFirstly = leftoversFacade.getLeftoversWhichMustBeUsedFirstly();
            ingredients = StorageMapper.mapFromLeftoversToIngredients(leftoversWhichMustBeUsedFirstly);
        }
        return ingredients;
    }

    public List<IngredientDto> updateStorageAndAddToGroceryList(List<IngredientDto> ingsInRecipe){
        // subtract from ings in recipe and update in maps
        List<IngredientDto> ingsWithoutUserIngs = userIngsFacade.removeUsedUserIngs(ingsInRecipe);
        groceryListFacade.addAllToGroceryList(ingsWithoutUserIngs);
        System.out.println("Grocery list: " + groceryListFacade.getGroceryList());
        return leftoversFacade.removeUsedLeftovers(ingsWithoutUserIngs);
    }

}
