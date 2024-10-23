package com.mealplannerv2.user;

import com.mealplannerv2.auth.dto.UserDto;
import com.mealplannerv2.user.model.SavedPrefers;
import com.mealplannerv2.user.model.PlannedRecipe;
import com.mealplannerv2.user.model.User;
import com.mealplannerv2.user.model.PlannedDay;
import com.mealplannerv2.user.model.Plan;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static java.lang.Math.abs;


@Component
@AllArgsConstructor
public class UserFacade {

    private final UserRepository repository;
    @Setter
    private Clock clock;

//    public UserDto getUserDto() {
//        String username = getAuthenticatedUser();
//        return repository.findByUsername(username)
//                .map(UserMapper::mapFromUserToUserDto)
//                .orElseThrow(() -> new BadCredentialsException("Username not found"));
//    }

    public List<Plan> getUserPlans() {
        User user = getUser();
        if(user.getPlans() == null){
            return new ArrayList<>();
        }
        return user.getPlans();
    }

    public SavedPrefers getSavedPrefers(String username) {
        UserDto userDto = getByUsername(username);
        return userDto.getPreferences();
    }

    public void updateSavedPrefers(String username, SavedPrefers newPrefers) {
        UserDto userDto = getByUsername(username);
        userDto.setPreferences(newPrefers);
        User user = UserMapper.mapFromUserDtoToUser(userDto);
        repository.save(user);
    }

    public List<String> getRecipesNames() {
        List<Plan> plans = getUserPlans();
        if (plans == null || plans.isEmpty()) {
            return Collections.emptyList();
        }
        return plans.stream()
                .flatMap(plan -> plan.plannedDays().stream())
                .map(PlannedDay::getPlannedRecipes)
                .flatMap(List::stream)
                .map(PlannedRecipe::recipeName)
                .toList();
    }

    public List<String> getRecipesNames(PlannedDay plannedDay) {
        return plannedDay.getPlannedRecipes().stream()
                .map(PlannedRecipe::recipeName)
                .toList();
    }

    public void saveNewPlan(String username, List<PlannedDay> tempDays) {
        UserDto userDto = getByUsername(username);
        List<Plan> plans = userDto.getPlans();
        if(plans == null){
            plans = new ArrayList<>();
        }
        Plan newPlan = new Plan(tempDays);
        plans.add(newPlan);
        plans.sort(Comparator.comparing(plan -> plan.plannedDays().get(0).getDate()));
        userDto.setPlans(plans);
        User user = UserMapper.mapFromUserDtoToUser(userDto);
        repository.save(user);
    }

    public void clearHistory() {
//        System.out.println("UserFacade clock: " + ZonedDateTime.now(clock));
        List<User> all = repository.findAll();
        for(User user : all) {
            List<Plan> newPlans = new ArrayList<>();
            List<Plan> plans = user.getPlans();
            if(plans != null){
                for(Plan plan : plans){
                    int lastDayIndex = plan.plannedDays().size() - 1;
                    long between = abs(ChronoUnit.DAYS.between(
                            LocalDate.now(clock),
                            plan.plannedDays().get(lastDayIndex).getDate()
                    ));
                    if(between <= 7){
                        newPlans.add(plan);
                    }
                }
                user.setPlans(newPlans);
                repository.save(user);
            }
        }
//        System.out.println("UserFacade: plans clearing finished.");
    }

    public UserDto getByUsername(String username) {
        return repository.findByUsername(username)
                .map(UserMapper::mapFromUserToUserDto)
                .orElseThrow(() -> new BadCredentialsException("Username not found"));
    }

    public UserDto getByEmail(String email) {
        return repository.findByEmail(email)
                .map(UserMapper::mapFromUserToUserDto)
                .orElseThrow(() -> new BadCredentialsException("Email not found"));
    }

//    public UserDto getById(String id) {
//        return repository.findById(id)
//                .map(UserMapper::mapFromUserToUserDto)
//                .orElseThrow(() -> new BadCredentialsException("User with id " + id + " not found"));
//    }

    private User getUser() {
        String username = getAuthenticatedUser();
        return repository.findByUsername(username)
                .orElseThrow(() -> new BadCredentialsException("Username not found"));
    }

    private String getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

}
