package com.mealplannerv2.recipe;

import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class DayPlan {
    private LocalDate date;
    private List<RecipeDay> planned_day;

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null) {
            return false;
        }
        if (!(other instanceof DayPlan otherDayPlan)) {
            return false;
        }
        return otherDayPlan.date.equals(this.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date);
    }
}
