package com.mealplannerv2.storage;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class StorageImpl implements IStorage{

    @Override
    public <T extends IngredientDto> void addItem(Map<String, T> map, T newProduct) {
        if (map.isEmpty()) {
            map.put(newProduct.getName(), newProduct);
        } else {
            IngredientDto keptProduct = map.get(newProduct.getName());
            if(keptProduct != null){
                // zał. units are equal
                double currentAmount = keptProduct.getAmount();
                double newAmount = currentAmount + newProduct.getAmount();
                keptProduct.setAmount(newAmount);
            } else {
                map.put(newProduct.getName(), newProduct);
            }
        }
    }

    @Override
    public List<IngredientDto> removeItem(Map<String, ? extends IngredientDto> map, List<IngredientDto> ingsFromRecipe) {
        List<IngredientDto> updatedRecipeIngs = new ArrayList<>();

        for(IngredientDto ingR: ingsFromRecipe){
            IngredientDto ingMap = map.get(ingR.getName());
            if(ingMap != null){
                double newAmount = ingR.getAmount() - ingMap.getAmount();
                if(newAmount > 0){
                    updatedRecipeIngs.add(new IngredientDto(ingR.getName(), newAmount, ingR.getUnit()));
                    map.remove(ingMap.getName());
                } else if(newAmount < 0){
                    ingMap.setAmount(Math.abs(newAmount));
                } else {
                    map.remove(ingMap.getName());
                }
            } else {
                updatedRecipeIngs.add(ingR);
            }
        }
        return updatedRecipeIngs;
    }
}
