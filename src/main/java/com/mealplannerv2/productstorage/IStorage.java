package com.mealplannerv2.productstorage;

import com.mealplannerv2.plangenerator.recipefilter.dto.IngredientDto;

import java.util.List;
import java.util.Map;

public interface IStorage {
    <T extends Storage> void addItem(Map<String, T> map, T newProduct);
    List<IngredientDto> removeItem(Map<String, ? extends Storage> map, List<IngredientDto> ings);
}
