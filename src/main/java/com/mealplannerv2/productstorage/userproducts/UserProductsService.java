package com.mealplannerv2.productstorage.userproducts;

import com.mealplannerv2.plangenerator.recipefilter.dto.IngredientDto;
import com.mealplannerv2.productstorage.StorageService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
class UserProductsService extends StorageService {
    private static final Map<String, UserProductDto> userProductsMap = new HashMap<>();

    public Map<String, UserProductDto> getStoredProducts(){
        return userProductsMap;
    }

    public void addAllUniq(List<UserProductDto> newProducts){
        for(UserProductDto product: newProducts){
            addUniq(product);
        }
    }

    public void addUniq(UserProductDto product){
        addItem(userProductsMap, product);
    }

    public List<IngredientDto> remove(List<IngredientDto> ings){
        return removeItem(userProductsMap, ings);
    }
}
