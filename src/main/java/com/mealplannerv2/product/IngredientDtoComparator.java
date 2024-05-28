package com.mealplannerv2.product;

import com.mealplannerv2.repository.IngredientDto;

import java.util.Comparator;

public class IngredientDtoComparator implements Comparator<IngredientDto> {

    @Override
    public int compare(IngredientDto o1, IngredientDto o2) {
        int compareDateResult = o1.getOpenDate().compareTo(o2.getOpenDate());
        if(compareDateResult == 0){
            int compareAmountResult = -o1.getAmount().compareTo(o2.getAmount());
            if(compareAmountResult == 0){
                return o1.getName().compareTo(o2.getName());
            }
            return compareAmountResult;
        }
        return compareDateResult;
    }
}
