package com.mealplannerv2.plangenerator.recipefilter.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.Objects;

@Data
public class IngredientDto {
    private String name;
    private Double amount;
    private String unit;
    private LocalDate openDate;

    public IngredientDto(String name, Double amount, String unit, LocalDate openDate) {
        this.name = name;
        this.amount = amount;
        this.unit = unit;
        this.openDate = openDate;
    }

    public IngredientDto(String name, Double amount, String unit) {
        this.name = name;
        this.amount = amount;
        this.unit = unit;
//        this.openDate = LocalDate.now();
    }

    public IngredientDto(String name, String unit) {
        this.name = name;
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
        if (!(other instanceof IngredientDto otherIng)) {
            return false;
        }
        return otherIng.name.equals(this.name) && otherIng.unit.equals(this.unit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, unit);
    }
}