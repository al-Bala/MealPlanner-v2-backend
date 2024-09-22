package com.mealplannerv2.plangenerator.infrastructure.controller;

import com.mealplannerv2.plangenerator.PlanGeneratorFacade;
import com.mealplannerv2.plangenerator.infrastructure.controller.dto.Preferences;
import com.mealplannerv2.plangenerator.infrastructure.controller.dto.UnchangingPrefers;
import com.mealplannerv2.plangenerator.infrastructure.controller.dto.request.FirstDayRequest;
import com.mealplannerv2.plangenerator.infrastructure.controller.dto.request.NextDayRequest;
import com.mealplannerv2.recipe.DayPlan;
import com.mealplannerv2.storage.IngredientDto;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/plan")
class PlanGeneratorController {

    private final PlanGeneratorFacade planGeneratorFacade;

//    @PostMapping("/day/first")
    @PostMapping("/firstDay")
    public ResponseEntity<DayPlan> firstPlanDay(@RequestBody FirstDayRequest firstDayRequest){
        System.out.println("Object: " + firstDayRequest);
        Preferences preferences = Preferences.builder()
                .unchangingPrefers(firstDayRequest.unchangingPrefers())
                .date(LocalDate.parse(firstDayRequest.date()))
                .mealsValues(firstDayRequest.mealsValues())
                .build();
        List<IngredientDto> userIngs = firstDayRequest.userProducts();
        DayPlan firstDayOfDayPlan = planGeneratorFacade.createFirstDayOfPlan(preferences, userIngs);
        System.out.println("RESULT: " + firstDayOfDayPlan);
        return ResponseEntity.ok(firstDayOfDayPlan);
    }

//    @PostMapping("/day/next")
    @PostMapping("/nextDay")
    public ResponseEntity<DayPlan> nextPlanDay(@RequestBody NextDayRequest nextDayRequest){
        Preferences preferences = getPreferences(nextDayRequest);
        DayPlan nextDayOfDayPlan = planGeneratorFacade.createNextDayOfPlan(preferences);
        return ResponseEntity.ok(nextDayOfDayPlan);
    }

//    @PostMapping("/day/change")
    @PostMapping("/changeDay")
    public ResponseEntity<DayPlan> changeDay(@RequestBody NextDayRequest nextDayRequest){
        Preferences preferences = getPreferences(nextDayRequest);
        DayPlan changedDay = planGeneratorFacade.changeLastDay(preferences);
        return ResponseEntity.ok(changedDay);
    }

//    @PostMapping("/save")
    @GetMapping("/savePlan")
    public ResponseEntity<String> savePlan(){
        planGeneratorFacade.savePlan();
        System.out.println("Plan saved");
        return ResponseEntity.ok("Saved new plan");
    }

    private static Preferences getPreferences(NextDayRequest nextDayRequest) {
        UnchangingPrefers unchangingPrefers = new UnchangingPrefers(
                "wegetariańska",
                2,
                List.of("kiwi")
        );
        return Preferences.builder()
                .unchangingPrefers(unchangingPrefers)
                .date(LocalDate.parse(nextDayRequest.date()))
                .mealsValues(nextDayRequest.mealsValues())
                .build();
    }

    @GetMapping("/test")
    public String test() {
        return "Correct ";
    }
}
