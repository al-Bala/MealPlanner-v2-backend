package pl.mealplanner.scheduler;

import com.mealplannerv2.user.UserFacade;
import com.mealplannerv2.user.planhistory.scheduler.PlanHistoryScheduler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import pl.mealplanner.BaseIntegrationTest;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;

public class HistoryCleaningTest extends BaseIntegrationTest {

    @Autowired
    Clock clock;

    @Autowired
    UserFacade userFacade;

    @Autowired
    PlanHistoryScheduler planHistoryScheduler = new PlanHistoryScheduler(userFacade);

    @Test
    @WithMockUser(username = "testUser")
    public void should_remove_plans_ending_more_than_7_days_earlier_when_scheduler_run_each_day() {

        // 1 day: nothing changed in plans
        LocalDate startDate = LocalDate.of(2024, 9, 17);
        setClockDate(startDate);
        planHistoryScheduler.clearHistory();
        assertThat(userFacade.getUserPlans().size()).isEqualTo(2);

        // 2 day: one plan left
        LocalDate day2 = LocalDate.now(clock).plusDays(1);
        setClockDate(day2);
        planHistoryScheduler.clearHistory();
        assertThat(userFacade.getUserPlans().size()).isEqualTo(1);

        // 3 day: plans is empty
        LocalDate day3 = LocalDate.now(clock).plusDays(1);
        setClockDate(day3);
        planHistoryScheduler.clearHistory();
        assertThat(userFacade.getUserPlans()).isEmpty();
    }

    private void setClockDate(LocalDate startDate) {
        clock = Clock.fixed(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
        userFacade.setClock(clock);
    }

}
