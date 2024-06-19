package com.mealplannerv2.productstorage;

import lombok.Data;

import java.util.Objects;

@Data
public class Storage {
    private String name;
    private Double amount;
    private String unit;

    public Storage(String name, String unit) {
        this.name = name;
        this.unit = unit;
    }

    public Storage(String name, Double amount, String unit) {
        this.name = name;
        this.amount = amount;
        this.unit = unit;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null) {
            return false;
        }
        if (!(other instanceof Storage otherProduct)) {
            return false;
        }
        return otherProduct.name.equals(this.name) && otherProduct.unit.equals(this.unit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, unit);
    }
}
