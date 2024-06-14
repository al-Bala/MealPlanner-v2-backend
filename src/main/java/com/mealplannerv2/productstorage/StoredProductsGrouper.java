package com.mealplannerv2.productstorage;

import com.mealplannerv2.productstorage.dto.StoredProductDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
class StoredProductsGrouper {

    private final StoredProductsService storedProductsService;

    public List<StoredProductDto> getProductsWithTheFewestDaysToSpoil(int daysToSpoil){
        List<StoredProductDto> productsWithGivenDaysToExpire = getProductsWithGivenDaysToSpoil(daysToSpoil);
        if(!productsWithGivenDaysToExpire.isEmpty()){
            return productsWithGivenDaysToExpire;
        }
        return getProductsWithTheFewestDaysToSpoil(++daysToSpoil);
    }

    private List<StoredProductDto> getProductsWithGivenDaysToSpoil(int daysToSpoil) {
        List<StoredProductDto> listOfProductsInUse = new ArrayList<>(storedProductsService.getStoredProducts().values());
        return listOfProductsInUse.stream()
                .filter(product -> product.getDaysToSpoilAfterOpening() == daysToSpoil)
                .toList();
    }
}
