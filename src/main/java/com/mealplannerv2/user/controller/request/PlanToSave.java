package com.mealplannerv2.user.controller.request;

import com.mealplannerv2.user.model.PlannedDay;

import java.util.List;

public record PlanToSave(
        String startDateText,
        List<PlannedDay> daysToSave
) {
}
