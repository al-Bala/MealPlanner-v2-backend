package com.mealplannerv2.storage.leftovers;

import com.mealplannerv2.storage.IngredientDto;
import com.mealplannerv2.storage.StorageImpl;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
class LeftoversService extends StorageImpl {
    private static final Map<String, LeftoverDto> leftoversMap = new HashMap<>();

    public Map<String, LeftoverDto> getLeftovers(){
        return leftoversMap;
    }

    public void addAllUniq(List<LeftoverDto> leftovers){
        for(LeftoverDto leftover: leftovers){
            addUniq(leftover);
        }
    }

    public void addUniq(LeftoverDto leftover){
        addItem(leftoversMap, leftover);
    }

    public List<IngredientDto> remove(List<IngredientDto> ingsDto){
        return removeItem(leftoversMap, ingsDto);
    }

    public void updateDaysToSpoilAfterOpening(){
        leftoversMap.values()
                .forEach(this::setUpdatedDaysToSpoil);
    }

    private void setUpdatedDaysToSpoil(LeftoverDto leftover){
        int daysToSpoilAfterOpening = leftover.getDaysToSpoilAfterOpening();
        int decreasedDays = --daysToSpoilAfterOpening;
        leftover.setDaysToSpoilAfterOpening(decreasedDays);
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
