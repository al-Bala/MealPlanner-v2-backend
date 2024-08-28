package com.mealplannerv2.user;

import com.mealplannerv2.auth.dto.UserDto;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserFacade {

    private final UserRepository repository;

    public UserDto findByUsername(String username) {
        return repository.findByUsername(username)
                .map(UserMapper::mapFromUserToUserDto)
                .orElseThrow(() -> new BadCredentialsException("Username not found"));
    }

    public UserDto getAuthenticatedUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return findByUsername(username);
    }

}
