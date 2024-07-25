package com.mealplannerv2.loginandregister.dto;

import com.mealplannerv2.plangenerator.infrastructure.controller.dto.SavedPreferences;
import com.mealplannerv2.recipe.PlannedDayDb;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;

import java.util.List;

@Data
@Builder
public class UserDto {
    private ObjectId id;
    private String username;
    private String email;
    private String password;
    private SavedPreferences preferences;
    List<PlannedDayDb> plan;
}
