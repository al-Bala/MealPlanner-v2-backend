package com.mealplannerv2.storage;

import java.util.List;
import java.util.Map;

public interface IStorage {
    <T extends IngredientDto> void addItem(Map<String, T> map, T newProduct);
    List<IngredientDto> removeItem(Map<String, ? extends IngredientDto> map, List<IngredientDto> ings);
}
