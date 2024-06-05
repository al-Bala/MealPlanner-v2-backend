package com.mealplannerv2.productstorage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Objects;

@Builder
@AllArgsConstructor
@Data
public class StoredProductDto {
    private String name;
    private Double amount;
    private String unit;
    private boolean isOpen;
    private LocalDate openDate;
    private int daysToSpoilAfterOpening;
    private int daysToExpire;

    public StoredProductDto(String name, Double amount, String unit, LocalDate openDate) {
        this.name = name;
        this.amount = amount;
        this.unit = unit;
        this.openDate = openDate;
    }

    public StoredProductDto(String name, Double amount, String unit) {
        this.name = name;
        this.amount = amount;
        this.unit = unit;
//        this.openDate = LocalDate.now();
    }

    public StoredProductDto(String name, String unit) {
        this.name = name;
        this.unit = unit;
    }

    public StoredProductDto(String name, String unit, int daysToSpoilAfterOpening) {
        this.name = name;
        this.unit = unit;
        this.daysToSpoilAfterOpening = daysToSpoilAfterOpening;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null) {
            return false;
        }
        if (!(other instanceof StoredProductDto otherProduct)) {
            return false;
        }
        return otherProduct.name.equals(this.name) && otherProduct.unit.equals(this.unit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, unit);
    }
}
