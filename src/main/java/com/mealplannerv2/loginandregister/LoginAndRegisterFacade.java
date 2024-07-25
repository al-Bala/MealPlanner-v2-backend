package com.mealplannerv2.loginandregister;

import com.mealplannerv2.loginandregister.dto.UserDto;
import com.mealplannerv2.loginandregister.infrastructure.controller.dto.RegisterUserDto;
import com.mealplannerv2.loginandregister.infrastructure.controller.dto.RegistrationResultDto;
import com.mealplannerv2.recipe.PlannedDayDb;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Log4j2
@AllArgsConstructor
@Component
public class LoginAndRegisterFacade {

    private final UserRepository repository;

    public UserDto findByUsername(String username) {
        return repository.findByUsername(username)
                .map(LoginAndRegisterMapper::mapFromUserToUserDto)
                .orElseThrow(() -> new BadCredentialsException("Username not found"));
    }

    public UserDto getAuthenticatedUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return findByUsername(username);
    }

    public RegistrationResultDto register(RegisterUserDto userDto) {
        final User user = User.builder()
                .username(userDto.username())
                .email(userDto.email())
                .password(userDto.password())
                .build();
        User savedUser = repository.save(user);
        log.info("User: " + savedUser.getUsername() + " added to database");
        return new RegistrationResultDto(savedUser.id.toString(), true, savedUser.username);
    }

    public List<PlannedDayDb> saveGeneratedRecipe(PlannedDayDb plannedDay){
        UserDto userDto = getAuthenticatedUser();
        userDto.getPlan().add(plannedDay);
        User savedUser = repository.save(LoginAndRegisterMapper.mapFromUserDtoToUser(userDto));
        return savedUser.planHistory;
    }
}
