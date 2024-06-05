package com.mealplannerv2.product;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Log4j2
@Component
public class ProductFacade {

    private final ProductRepository productRepository;

    public Integer getMaxDaysAfterOpeningFromDb(String userProductName){
        Product productDb = productRepository.getProductByName(userProductName);
        return productDb.maxDaysAfterOpening();
    }

}
