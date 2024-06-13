package com.mealplannerv2.product.unit;

import lombok.Getter;

@Getter
public enum UnitEnum {

    GRAM("g"),
    DECAGRAM("dag"),
    KILOGRAM("kg"),
    MILLILITRE("ml"),
    LITRE("l"),
    PIECE("szt."),
    CUP("szklanka"),
    TABLESPOON("łyżka"),
    TEASPOON("łyżeczka");

    private final String unit;

    UnitEnum(String unit) {
        this.unit = unit;
    }
}
