package com.mealplannerv2.user.planhistory.scheduler;

import com.mealplannerv2.user.UserFacade;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PlanHistoryScheduler {

    private final UserFacade userFacade;

//    @Scheduled(fixedDelayString = "${history.scheduler.request.delay}")
//    @Scheduled(cron = "0 1 * * * *") // at 01:00 AM
    @Scheduled(cron = "${history.scheduler.occurrence}")
    public void clearHistory(){

        // Dla utrzymania wydajności bazy
        // - indeksy
        // - batchSize
        // - asynchroniczne zadania @Async

        System.out.println("Scheduler");
        userFacade.clearHistory();
    }
}
