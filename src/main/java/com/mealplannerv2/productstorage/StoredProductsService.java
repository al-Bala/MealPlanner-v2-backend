package com.mealplannerv2.productstorage;

import com.mealplannerv2.plangenerator.recipefilter.dto.IngredientDto;
import com.mealplannerv2.productstorage.dto.StoredProductDto;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
class StoredProductsService {
    private static final Map<String, StoredProductDto> storedProductsMap = new HashMap<>();

    public Map<String, StoredProductDto> getStoredProducts(){
        return storedProductsMap;
    }

    public void addAllUniq(List<StoredProductDto> newProducts){
        for(StoredProductDto product: newProducts){
            addUniq(product);
        }
    }

    public void addUniq(StoredProductDto newProduct){
        if (storedProductsMap.isEmpty()) {
            storedProductsMap.put(newProduct.getName(), newProduct);
        } else {
            StoredProductDto keptProduct = storedProductsMap.get(newProduct.getName());
            if(keptProduct != null){
                // zał. units are equal
                double currentAmount = keptProduct.getAmountToUse();
                double newAmount = currentAmount + newProduct.getAmountToUse();
                keptProduct.setAmountToUse(newAmount);
            } else {
                storedProductsMap.put(newProduct.getName(), newProduct);
            }
        }
    }

    public List<IngredientDto> remove(List<IngredientDto> ings){
        List<IngredientDto> updatedIngs = new ArrayList<>();

        for(IngredientDto ing: ings){
            StoredProductDto storedProductDto = storedProductsMap.get(ing.getName());
            if(storedProductDto != null){
                double newAmount = ing.getAmount() - storedProductDto.getAmountToUse();
                if(newAmount > 0){
                    updatedIngs.add(new IngredientDto(ing.getName(), newAmount, ing.getUnit()));
//                    ing.setAmount(newAmount);
                    storedProductsMap.remove(storedProductDto.getName());
                } else if(newAmount < 0){
//                    ings.removeIf(ings::contains);
                    storedProductDto.setAmountToUse(Math.abs(newAmount));
                } else {
//                    ings.removeIf(ings::contains);
                    storedProductsMap.remove(storedProductDto.getName());
                }
            } else {
                updatedIngs.add(ing);
            }
        }
        return updatedIngs;
    }

    public void updateDaysToSpoilAfterOpening(){
        storedProductsMap.values()
                .forEach(this::setUpdatedDaysToSpoil);
    }

    private void setUpdatedDaysToSpoil(StoredProductDto product){
        int daysToSpoilAfterOpening = product.getDaysToSpoilAfterOpening();
        int decreasedDays = --daysToSpoilAfterOpening;
        product.setDaysToSpoilAfterOpening(decreasedDays);
    }
}
