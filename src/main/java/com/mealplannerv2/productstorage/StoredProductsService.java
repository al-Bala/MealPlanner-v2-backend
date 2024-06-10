package com.mealplannerv2.productstorage;

import com.mealplannerv2.plangenerator.UserProduct;
import com.mealplannerv2.product.ProductFacade;
import com.mealplannerv2.productstorage.dto.StoredProductDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@AllArgsConstructor
@Service
class StoredProductsService {

//    private static final Comparator<StoredProduct> comparator = new StoredProductComparator();
//    private static final Set<StoredProduct> storedProducts = new TreeSet<>(comparator);
    private static final Set<StoredProductDto> storedProducts = new HashSet<>();
    private final ProductFacade productFacade;

    public Set<StoredProductDto> getStoredProducts(){
        return storedProducts;
    }

    public List<StoredProductDto> createNewForStoredProducts(List<UserProduct> userProducts){
        return userProducts.stream()
                .map(this::createOneNewForStoredProduct)
                .toList();
    }

    public StoredProductDto createOneNewForStoredProduct(UserProduct userProduct){
        return StoredProductDto.builder()
                .name(userProduct.name())
                .amount(userProduct.amount())
                .unit(userProduct.unit())
                // wartość ustaawiana na podstwie danych z bazy o produkcie
                .daysToSpoilAfterOpening(productFacade.getMaxDaysAfterOpeningFromDb(userProduct.name()))
                .build();
    }

    public void addAllUniq(List<StoredProductDto> newProducts){
        for(StoredProductDto product: newProducts){
            addUniq(product);
        }
    }

    public void addUniq(StoredProductDto newProduct){
        if (storedProducts.isEmpty()) {
            storedProducts.add(newProduct);
        } else {
            int i = 0;
            for (StoredProductDto keptProduct : storedProducts) {
                if (keptProduct.equals(newProduct)) {
                    double currentAmount = keptProduct.getAmount();
                    double newAmount = currentAmount + newProduct.getAmount();
                    keptProduct.setAmount(newAmount);
                    break;
                }
                i++;
            }
            if (i == storedProducts.size()) {
                storedProducts.add(newProduct);
            }
        }
    }

    public void updateDaysToSpoilAfterOpening(){
        storedProducts.forEach(this::setUpdatedDaysToSpoil);
    }

    private void setUpdatedDaysToSpoil(StoredProductDto product){
        int daysToSpoilAfterOpening = product.getDaysToSpoilAfterOpening();
        int decreasedDays = --daysToSpoilAfterOpening;
        product.setDaysToSpoilAfterOpening(decreasedDays);
    }
}
