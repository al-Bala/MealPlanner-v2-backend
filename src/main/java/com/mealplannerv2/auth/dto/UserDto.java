package com.mealplannerv2.auth.dto;

import com.mealplannerv2.user.model.SavedPrefers;
import com.mealplannerv2.user.model.Plan;
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
    private SavedPrefers preferences;
    private List<Plan> plans;
}
