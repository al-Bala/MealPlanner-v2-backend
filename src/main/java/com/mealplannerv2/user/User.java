package com.mealplannerv2.user;

import com.mealplannerv2.plangenerator.infrastructure.controller.dto.SavedPrefers;
import com.mealplannerv2.recipe.Plan;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@Builder
@Document(collection = "users")
public class User implements UserDetails {
    @Id
    ObjectId id;

    @Field("username")
    @Indexed(unique = true)
    String username;

    @Field("email")
    @Indexed(unique = true)
    String email;

    @Field("password")
    String password;

    @Field("preferences")
    SavedPrefers preferences;

    @Field("plans")
    List<Plan> plans;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
