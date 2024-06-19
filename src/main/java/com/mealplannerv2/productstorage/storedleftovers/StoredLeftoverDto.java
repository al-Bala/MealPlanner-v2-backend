package com.mealplannerv2.productstorage.storedleftovers;

import com.mealplannerv2.productstorage.Storage;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class StoredLeftoverDto extends Storage {
    private int daysToSpoilAfterOpening;

    public StoredLeftoverDto(String name, String unit) {
        super(name, unit);
    }

    public StoredLeftoverDto(String name, Double amount, String unit) {
        super(name, amount, unit);
    }

    public StoredLeftoverDto(String name, Double amount, String unit, int daysToSpoilAfterOpening) {
        super(name, amount, unit);
        this.daysToSpoilAfterOpening = daysToSpoilAfterOpening;
    }
}
