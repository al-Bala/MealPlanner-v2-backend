package com.mealplannerv2.infrastructure.security.jwt;

import com.mealplannerv2.auth.dto.UserDto;
import com.mealplannerv2.user.UserFacade;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Collections;

@AllArgsConstructor
class LoginUserDetailsService implements UserDetailsService {

    private final UserFacade userFacade;

    @Override
    public UserDetails loadUserByUsername(String username) throws BadCredentialsException {
        UserDto userDto = userFacade.findByUsername(username);
        return getUser(userDto);
    }

    private org.springframework.security.core.userdetails.User getUser(UserDto userDto) {
        return new org.springframework.security.core.userdetails.User(
                userDto.getUsername(),
                userDto.getPassword(),
                Collections.emptyList());
    }
}
