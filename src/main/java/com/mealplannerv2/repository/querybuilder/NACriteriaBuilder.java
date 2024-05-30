package com.mealplannerv2.repository.querybuilder;

import com.mealplannerv2.repository.IngredientDto;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.List;

public interface NACriteriaBuilder {
    Criteria getCriteria(List<IngredientDto> userProducts);
}
