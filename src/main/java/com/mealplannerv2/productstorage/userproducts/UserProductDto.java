package com.mealplannerv2.productstorage.userproducts;

import com.mealplannerv2.productstorage.Storage;

public class UserProductDto extends Storage {

    public UserProductDto(String name, String unit) {
        super(name, unit);
    }

    public UserProductDto(String name, Double amount, String unit) {
        super(name, amount, unit);
    }

}
