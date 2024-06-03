package com.mealplannerv2.user;

import com.mealplannerv2.plangenerator.recipefilter.dto.IngredientDto;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Set;

@Document("users")
public record User(
        @Id ObjectId id,
        @Field("login") String login,
        @Field("email") String email,
        @Field("password") String password,
        @Field("products_in_use") Set<IngredientDto> productsInUse
) {
}
