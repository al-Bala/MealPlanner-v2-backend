package com.mealplannerv2.user.model;

import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class PlannedDay {
    private LocalDate date;
    private List<PlannedRecipe> plannedRecipes;

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null) {
            return false;
        }
        if (!(other instanceof PlannedDay otherPlannedDay)) {
            return false;
        }
        return otherPlannedDay.date.equals(this.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date);
    }
}
