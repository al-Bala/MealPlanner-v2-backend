package com.mealplannerv2.plangenerator.infrastructure.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
public class SavedPrefers {
    private String diet;
    private int portions;
    private List<String> products_to_avoid;

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null) {
            return false;
        }
        if (!(other instanceof SavedPrefers otherPrefs)) {
            return false;
        }
        return
                otherPrefs.diet.equals(this.diet) &&
                otherPrefs.portions == this.portions &&
                new HashSet<>(otherPrefs.products_to_avoid).containsAll(this.products_to_avoid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(diet, portions, products_to_avoid);
    }
}
