package com.mealplannerv2.productstorage.storedleftovers;

import com.mealplannerv2.plangenerator.recipefilter.dto.IngredientDto;
import com.mealplannerv2.productstorage.StorageService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
class StoredLeftoversService extends StorageService {
    private static final Map<String, StoredLeftoverDto> storedProductsMap = new HashMap<>();

    public Map<String, StoredLeftoverDto> getStoredProducts(){
        return storedProductsMap;
    }

    public void addAllUniq(List<StoredLeftoverDto> newProducts){
        for(StoredLeftoverDto product: newProducts){
            addUniq(product);
        }
    }

    public void addUniq(StoredLeftoverDto product){
        addItem(storedProductsMap, product);
    }

    public List<IngredientDto> remove(List<IngredientDto> ings){
        return removeItem(storedProductsMap, ings);
    }

    public void updateDaysToSpoilAfterOpening(){
        storedProductsMap.values()
                .forEach(this::setUpdatedDaysToSpoil);
    }

    private void setUpdatedDaysToSpoil(StoredLeftoverDto product){
        int daysToSpoilAfterOpening = product.getDaysToSpoilAfterOpening();
        int decreasedDays = --daysToSpoilAfterOpening;
        product.setDaysToSpoilAfterOpening(decreasedDays);
    }

//    public void addUniq(StoredProductDto newProduct){
//        if (storedProductsMap.isEmpty()) {
//            storedProductsMap.put(newProduct.getName(), newProduct);
//        } else {
//            StoredProductDto keptProduct = storedProductsMap.get(newProduct.getName());
//            if(keptProduct != null){
//                // zał. units are equal
//                double currentAmount = keptProduct.getAmountToUse();
//                double newAmount = currentAmount + newProduct.getAmountToUse();
//                keptProduct.setAmountToUse(newAmount);
//            } else {
//                storedProductsMap.put(newProduct.getName(), newProduct);
//            }
//        }
//    }
//
//    public List<IngredientDto> remove(List<IngredientDto> ings){
//        List<IngredientDto> updatedIngs = new ArrayList<>();
//
//        for(IngredientDto ing: ings){
//            StoredProductDto storedProductDto = storedProductsMap.get(ing.getName());
//            if(storedProductDto != null){
//                double newAmount = ing.getAmount() - storedProductDto.getAmountToUse();
//                if(newAmount > 0){
//                    updatedIngs.add(new IngredientDto(ing.getName(), newAmount, ing.getUnit()));
////                    ing.setAmount(newAmount);
//                    storedProductsMap.remove(storedProductDto.getName());
//                } else if(newAmount < 0){
////                    ings.removeIf(ings::contains);
//                    storedProductDto.setAmountToUse(Math.abs(newAmount));
//                } else {
////                    ings.removeIf(ings::contains);
//                    storedProductsMap.remove(storedProductDto.getName());
//                }
//            } else {
//                updatedIngs.add(ing);
//            }
//        }
//        return updatedIngs;
//    }
}
