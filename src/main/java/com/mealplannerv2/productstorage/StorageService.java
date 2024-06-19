package com.mealplannerv2.productstorage;

import com.mealplannerv2.plangenerator.recipefilter.dto.IngredientDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class StorageService implements IStorage{

    @Override
    public <T extends Storage> void addItem(Map<String, T> map, T newProduct) {
        if (map.isEmpty()) {
            map.put(newProduct.getName(), newProduct);
        } else {
            Storage keptProduct = map.get(newProduct.getName());
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
    public List<IngredientDto> removeItem(Map<String, ? extends Storage> map, List<IngredientDto> ings) {
        List<IngredientDto> updatedIngs = new ArrayList<>();

        for(IngredientDto ing: ings){
            Storage storedProductDto = map.get(ing.getName());
            if(storedProductDto != null){
                double newAmount = ing.getAmount() - storedProductDto.getAmount();
                if(newAmount > 0){
                    updatedIngs.add(new IngredientDto(ing.getName(), newAmount, ing.getUnit()));
                    map.remove(storedProductDto.getName());
                } else if(newAmount < 0){
                    storedProductDto.setAmount(Math.abs(newAmount));
                } else {
                    map.remove(storedProductDto.getName());
                }
            } else {
                updatedIngs.add(ing);
            }
        }
        return updatedIngs;
    }
}
