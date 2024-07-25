package com.mealplannerv2.storage.leftovers;

import com.mealplannerv2.storage.IngredientDto;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LeftoverDto extends IngredientDto {
    private int daysToSpoilAfterOpening;

    public LeftoverDto(String name, String unit) {
        super(name, unit);
    }

    public LeftoverDto(String name, Double amount, String unit) {
        super(name, amount, unit);
    }

    public LeftoverDto(String name, Double amount, String unit, int daysToSpoilAfterOpening) {
        super(name, amount, unit);
        this.daysToSpoilAfterOpening = daysToSpoilAfterOpening;
    }

    @Override
    public String toString() {
        return "LeftoverDto{" +
                "name='" + getName() + '\'' +
                ", amount=" + getAmount() +
                ", unit='" + getUnit() + '\'' +
                ", daysToSpoilAfterOpening=" + daysToSpoilAfterOpening +
                '}';
    }
}
