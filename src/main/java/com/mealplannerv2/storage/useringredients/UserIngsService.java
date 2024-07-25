package com.mealplannerv2.storage.useringredients;

import com.mealplannerv2.storage.IngredientDto;
import com.mealplannerv2.storage.StorageImpl;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
class UserIngsService extends StorageImpl {
    private static final Map<String, IngredientDto> userIngsMap = new HashMap<>();

    public Map<String, IngredientDto> getUserIngs(){
        return userIngsMap;
    }

    public void addAllUniq(List<IngredientDto> newIngs){
        for(IngredientDto product: newIngs){
            addUniq(product);
        }
    }

    public void addUniq(IngredientDto product){
        addItem(userIngsMap, product);
    }

    public List<IngredientDto> remove(List<IngredientDto> ings){
        return removeItem(userIngsMap, ings);
    }
}
