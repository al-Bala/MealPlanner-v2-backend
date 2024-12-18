package com.mealplannerv2.recipe.model;

import lombok.Builder;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Builder
@Document("recipes")
public record Recipe(
        @Id ObjectId id,
        @Field("name") String name,
        @Field("portions") int portions,
        @Field("prepare_time") int prepareTimeMin,
        @Field("max_storage_time") int maxStorageTimeDays,
        @Field("dietId") String dietId,
        @Field("ingredients") List<Ingredient> ingredients,
        @Field("steps") List<String> steps
) {
}
