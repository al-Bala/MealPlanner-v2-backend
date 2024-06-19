package com.mealplannerv2.productstorage.storedleftovers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
class StoredLeftoversGrouper {

    private final StoredLeftoversService storedLeftoversService;

    public List<StoredLeftoverDto> getProductsWithTheFewestDaysToSpoil(int daysToSpoil){
        List<StoredLeftoverDto> productsWithGivenDaysToExpire = getProductsWithGivenDaysToSpoil(daysToSpoil);
        if(!productsWithGivenDaysToExpire.isEmpty()){
            return productsWithGivenDaysToExpire;
        }
        return getProductsWithTheFewestDaysToSpoil(++daysToSpoil);
    }

    private List<StoredLeftoverDto> getProductsWithGivenDaysToSpoil(int daysToSpoil) {
        List<StoredLeftoverDto> listOfProductsInUse = new ArrayList<>(storedLeftoversService.getStoredProducts().values());
        return listOfProductsInUse.stream()
                .filter(product -> product.getDaysToSpoilAfterOpening() == daysToSpoil)
                .toList();
    }
}
