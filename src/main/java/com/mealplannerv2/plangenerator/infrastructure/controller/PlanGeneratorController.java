package com.mealplannerv2.plangenerator.infrastructure.controller;

import com.mealplannerv2.loginandregister.LoginAndRegisterFacade;
import com.mealplannerv2.loginandregister.dto.UserDto;
import com.mealplannerv2.plangenerator.DataForRecipeFiltering;
import com.mealplannerv2.plangenerator.PlanGeneratorFacade;
import com.mealplannerv2.plangenerator.PlannedDay;
import com.mealplannerv2.plangenerator.infrastructure.controller.dto.DayInfo;
import com.mealplannerv2.plangenerator.infrastructure.controller.dto.PreferencesInfo;
import com.mealplannerv2.plangenerator.recipefilter.RecipeFetcherFacade;
import com.mealplannerv2.plangenerator.recipefilter.dto.RecipeDto;
import com.mealplannerv2.plangenerator.recipefilter.model.Ingredient;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@AllArgsConstructor
@Controller
@RequestMapping("/plan")
class PlanGeneratorController {

    private final PlanGeneratorFacade planGeneratorFacade;
    private final RecipeFetcherFacade recipeFetcherFacade;
    private final LoginAndRegisterFacade loginAndRegisterFacade;

//    @GetMapping("/preferences")
//    public ResponseEntity<SavedPreferences> getPreferencesForm(){
//        // if data is saved in database
//        // return diet, portions and productsToAvoid
//        UserDto user = loginAndRegisterFacade.getAuthenticatedUser();
//        return ResponseEntity.ok(user.preferences());
//    }

    @PostMapping("/preferences")
    public ResponseEntity<PreferencesInfo> postInfo(@RequestBody PreferencesInfo preferencesInfo, DayInfo dayInfo){
        // if data not saved,
        // save diet, portions and productsToAvoid in database for user
//        UserDto user = loginAndRegisterFacade.getAuthenticatedUser();
//        if(user.preferences() == null){
//        }

        List<PlannedDay> firstDayOfPlan = planGeneratorFacade.createFirstDayOfPlan(preferencesInfo, dayInfo);

        return ResponseEntity.ok(preferencesInfo);
    }


    @GetMapping("")
    public ResponseEntity<PlanResponseDto> find() {
        DataForRecipeFiltering info = DataForRecipeFiltering.builder()
                .forHowManyDays(1)
//                .diet("wegetariańska")
//                .timeForPrepareMin(5)
//                .ingredientsToUseFirstly(List.of("marchew"))
//                .productsToAvoid(List.of("oliwki"))
                .ingredientsToUseFirstly(List.of(
                        new Ingredient("truskawki", 100.0, ""),
                        new Ingredient("marchew", 2.0, ""),
                        new Ingredient("ryż", 300.0, "")))
                .build();

        RecipeDto result = recipeFetcherFacade.fetchRecipeByPreferences(info);
        System.out.println(result);
        return ResponseEntity.ok(new PlanResponseDto(result));
    }

}
