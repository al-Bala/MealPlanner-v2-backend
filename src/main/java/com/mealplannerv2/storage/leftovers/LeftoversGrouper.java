package com.mealplannerv2.storage.leftovers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
class LeftoversGrouper {

    private final LeftoversService leftoversService;

    public List<LeftoverDto> getLeftoversWithTheFewestDaysToSpoil(int daysToSpoil){
        List<LeftoverDto> productsWithGivenDaysToExpire = getLeftoversWithGivenDaysToSpoil(daysToSpoil);
        if(!productsWithGivenDaysToExpire.isEmpty()){
            return productsWithGivenDaysToExpire;
        }
        return getLeftoversWithTheFewestDaysToSpoil(++daysToSpoil);
    }

    private List<LeftoverDto> getLeftoversWithGivenDaysToSpoil(int daysToSpoil) {
        List<LeftoverDto> listOfProductsInUse = new ArrayList<>(leftoversService.getLeftovers().values());
        return listOfProductsInUse.stream()
                .filter(product -> product.getDaysToSpoilAfterOpening() == daysToSpoil)
                .toList();
    }
}
