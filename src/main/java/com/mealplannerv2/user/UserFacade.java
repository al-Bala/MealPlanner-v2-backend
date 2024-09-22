package com.mealplannerv2.user;

import com.mealplannerv2.auth.dto.UserDto;
import com.mealplannerv2.recipe.DayPlan;
import com.mealplannerv2.recipe.History;
import com.mealplannerv2.recipe.Plan;
import com.mealplannerv2.recipe.RecipeDay;
import com.mealplannerv2.user.planhistory.error.DuplicatePlanException;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Math.abs;


@Component
@AllArgsConstructor
public class UserFacade {

    private final UserRepository repository;
    @Setter
    private Clock clock;

    public UserDto getUserDto() {
        String username = getAuthenticatedUser();
        return repository.findByUsername(username)
                .map(UserMapper::mapFromUserToUserDto)
                .orElseThrow(() -> new BadCredentialsException("Username not found"));
    }

    public History getHistory() {
        User user = getUser();
        return user.getHistory();
    }

    public List<String> getRecipesNames() {
        History history = getHistory();
        if (history == null || history.plans().isEmpty()) {
            return Collections.emptyList();
        }
        return history.plans().stream()
                .flatMap(plan -> plan.days().stream())
                .map(DayPlan::getPlanned_day)
                .flatMap(List::stream)
                .map(RecipeDay::recipeName)
                .toList();
    }

    public List<String> getRecipesNames(DayPlan plannedDay) {
        return plannedDay.getPlanned_day().stream()
                .map(RecipeDay::recipeName)
                .toList();
    }

    public void saveNewPlan(Plan tempPlan) {
        User user = getUser();
        History history = user.getHistory();
        if(history == null){
            history = new History(new ArrayList<>());
        }
        for(Plan plan : history.plans()){
            Set<LocalDate> dates = plan.days().stream()
                    .map(DayPlan::getDate)
                    .collect(Collectors.toSet());
            Set<LocalDate> tempDates = tempPlan.days().stream()
                    .map(DayPlan::getDate)
                    .collect(Collectors.toSet());
            if(dates.containsAll(tempDates) && plan.days().size() == tempPlan.days().size()){
                throw new DuplicatePlanException("There is a duplicate plan in history.");
            }
        }
        history.plans().add(tempPlan);
        history.plans().sort(Comparator.comparing(plan -> plan.days().get(0).getDate()));
        user.setHistory(history);
        repository.save(user);
    }

    public void clearHistory() {
//        System.out.println("UserFacade clock: " + ZonedDateTime.now(clock));
        List<User> all = repository.findAll();
        for(User user : all) {
            History newHistory = new History(new ArrayList<>());
            History history = user.getHistory();
            if(history != null){
                for(Plan plan : history.plans()){
                    int lastDayIndex = plan.days().size() - 1;
                    long between = abs(ChronoUnit.DAYS.between(
                            LocalDate.now(clock),
                            plan.days().get(lastDayIndex).getDate()
                    ));
                    if(between <= 7){
                        newHistory.plans().add(plan);
                    }
                }
                user.setHistory(newHistory);
                repository.save(user);
            }
        }
//        System.out.println("UserFacade: history clearing finished.");
    }

    public UserDto getByUsername(String username) {
        return repository.findByUsername(username)
                .map(UserMapper::mapFromUserToUserDto)
                .orElseThrow(() -> new BadCredentialsException("Username not found"));
    }

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
