package com.mealplannerv2;

import com.mealplannerv2.plangenerator.recipefilter.dto.UserProduct;
import lombok.Builder;

import java.util.List;

@Builder(toBuilder = true)
public record InfoFromUser(
        int forHowManyDays,
        String diet,
        int timeForPrepareMin,
        List<UserProduct> userProducts,
        List<String> productsToAvoid
) {
}
