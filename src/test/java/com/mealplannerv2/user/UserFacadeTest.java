package com.mealplannerv2.user;

import com.mealplannerv2.auth.dto.UserDto;
import com.mealplannerv2.recipe.DayPlan;
import com.mealplannerv2.recipe.History;
import com.mealplannerv2.recipe.Plan;
import com.mealplannerv2.recipe.RecipeDay;
import com.mealplannerv2.user.planhistory.error.DuplicatePlanException;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserFacadeTest {

    // @Configuration i tworzenie @Bean --> createForTests()

    @Mock
    Clock clock;

    private final UserRepository userRepository = new UserRepositoryTestImpl();
    private final UserFacade userFacade = new UserFacade(userRepository, clock);
    private User user;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @BeforeEach
    void setUp() {
        user = new User(
                new ObjectId(),
                "user",
                "email",
                "pwd",
                null,
                null
        );
        userRepository.deleteAll();
        userRepository.save(user);

        LocalDate currentDate = LocalDate.of(2024, 9, 15);
        clock = Clock.fixed(currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
        userFacade.setClock(clock);

        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("user");
    }

    // DIFFERENT DATES
    @Test
    void should_add_new_plan_when_history_is_null() {
        // given
        Plan planToAdd = new Plan(List.of(
                new DayPlan(LocalDate.of(2024, 9, 15), List.of(new RecipeDay()))
        ));
        // when
        userFacade.saveNewPlan(planToAdd);
        // then
        UserDto user = userFacade.getByUsername("user");
        assertThat(user.getHistory().plans().size()).isEqualTo(1);
        printPlan(user);
    }

    @Test
    void should_add_next_different_plan_when_history_is_not_empty() {
        // given
        Plan plan = new Plan(List.of(
                new DayPlan(LocalDate.of(2024, 9, 15), List.of(new RecipeDay()))
        ));
        History history = new History(new ArrayList<>(List.of(plan)));
        user.setHistory(history);

        Plan planToAdd = new Plan(List.of(
                new DayPlan(LocalDate.of(2024, 9, 16), List.of(new RecipeDay()))
        ));
        // when
        userFacade.saveNewPlan(planToAdd);
        // then
        UserDto user = userFacade.getByUsername("user");
        assertThat(user.getHistory().plans().size()).isEqualTo(2);
        printPlan(user);
    }

    // REPEATED DATES
    @Test
    void should_add_plan_when_at_least_one_date_is_different() {
        // given
        Plan plan = new Plan(List.of(
                new DayPlan(LocalDate.of(2024, 9, 15), List.of(new RecipeDay())),
                new DayPlan(LocalDate.of(2024, 9, 16), List.of(new RecipeDay()))
        ));
        History history = new History(new ArrayList<>(List.of(plan)));
        user.setHistory(history);

        Plan planToAdd = new Plan(List.of(
                new DayPlan(LocalDate.of(2024, 9, 16), List.of(new RecipeDay())),
                new DayPlan(LocalDate.of(2024, 9, 17), List.of(new RecipeDay()))
        ));
        // when
        userFacade.saveNewPlan(planToAdd);
        // then
        UserDto byUsername = userFacade.getByUsername("user");
        assertThat(user.getHistory().plans().size()).isEqualTo(2);
        printPlan(byUsername);
    }

    @Test
    void should_throw_exception_when_two_plans_have_all_dates_the_same() {
        // given
        Plan plan = new Plan(List.of(
                new DayPlan(LocalDate.of(2024, 9, 15), List.of(new RecipeDay())),
                new DayPlan(LocalDate.of(2024, 9, 16), List.of(new RecipeDay()))
        ));
        History history = new History(new ArrayList<>(List.of(plan)));
        user.setHistory(history);

        Plan planToAdd = new Plan(List.of(
                new DayPlan(LocalDate.of(2024, 9, 15), List.of(new RecipeDay())),
                new DayPlan(LocalDate.of(2024, 9, 16), List.of(new RecipeDay()))
        ));
        // when & then
        Assertions.assertThrows(DuplicatePlanException.class, () -> userFacade.saveNewPlan(planToAdd));
    }

    @Test
    void should_sort_plans_by_start_dates_in_ascending_order() {
        // given
        Plan plan1 = new Plan(List.of(
                new DayPlan(LocalDate.of(2024, 9, 16), List.of(new RecipeDay()))
        ));
        Plan plan2 = new Plan(List.of(
                new DayPlan(LocalDate.of(2024, 9, 17), List.of(new RecipeDay())),
                new DayPlan(LocalDate.of(2024, 9, 18), List.of(new RecipeDay()))
        ));
        Plan plan3 = new Plan(List.of(
                new DayPlan(LocalDate.of(2024, 9, 20), List.of(new RecipeDay()))
        ));
        History history = new History(new ArrayList<>(List.of(plan1, plan2, plan3)));
        user.setHistory(history);

        Plan planToAdd = new Plan(List.of(
                new DayPlan(LocalDate.of(2024, 9, 18), List.of(new RecipeDay())),
                new DayPlan(LocalDate.of(2024, 9, 19), List.of(new RecipeDay()))
        ));
        // when
        userFacade.saveNewPlan(planToAdd);
        // then
        UserDto byUsername = userFacade.getByUsername("user");
        assertThat(user.getHistory().plans().size()).isEqualTo(4);
        assertAll(
                () -> assertEquals(LocalDate.of(2024, 9, 16), getStartDate(0)),
                () -> assertEquals(LocalDate.of(2024, 9, 17), getStartDate(1)),
                () -> assertEquals(LocalDate.of(2024, 9, 18), getStartDate(2)),
                () -> assertEquals(LocalDate.of(2024, 9, 20), getStartDate(3))
        );
        printPlan(byUsername);
    }

    private LocalDate getStartDate(int planIndex) {
        return user.getHistory().plans().get(planIndex).days().get(0).getDate();
    }

    private static void printPlan(UserDto user) {
        user.getHistory().plans()
                .forEach(p -> {
                    System.out.println("Plan: ");
                    p.days()
                            .forEach(d -> System.out.println(d.getDate() + " " + d.getPlanned_day()));
                });
    }
}