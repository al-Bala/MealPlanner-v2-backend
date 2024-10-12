package com.mealplannerv2.user.model;

import java.util.List;

public record Plan(
        List<PlannedDay> plannedDays
) {
}
