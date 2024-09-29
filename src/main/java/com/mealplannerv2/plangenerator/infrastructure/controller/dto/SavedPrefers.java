package com.mealplannerv2.plangenerator.infrastructure.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
public class SavedPrefers {
    private String dietId;
    private Integer portions;
    private List<String> products_to_avoid;

    public SavedPrefers(){
    }

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
                otherPrefs.dietId.equals(this.dietId) &&
                otherPrefs.portions.equals(this.portions) &&
                new HashSet<>(otherPrefs.products_to_avoid).containsAll(this.products_to_avoid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dietId, portions, products_to_avoid);
    }
}
