package com.mealplannerv2.user.controller;

import com.mealplannerv2.auth.dto.UserDto;
import com.mealplannerv2.user.model.SavedPrefers;
import com.mealplannerv2.user.UserFacade;
import com.mealplannerv2.user.UserRepository;
import com.mealplannerv2.user.controller.request.PlanToSave;
import com.mealplannerv2.user.model.User;
import com.mealplannerv2.user.model.PlannedDay;
import com.mealplannerv2.user.model.Plan;
import com.mealplannerv2.user.controller.response.Profile;
import com.mealplannerv2.user.planhistory.error.DuplicatePlanException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;
    private final UserFacade userFacade;

    @GetMapping("")
    public List<String> users() {
        List<User> all = userRepository.findAll();
        return all.stream()
                .map(User::getUsername)
                .toList();
    }

    @GetMapping("/{userId}/profile")
    public ResponseEntity<Profile> showProfile(@PathVariable String userId) {
        UserDto userDto = userFacade.getById(userId);
        Profile profile = Profile.builder()
                .username(userDto.getUsername())
                .plans(userDto.getPlans())
                .build();
        return ResponseEntity.ok(profile);
    }

    @GetMapping("/{userId}/prefs")
    public ResponseEntity<SavedPrefers> getSavedPreferences(@PathVariable String userId){
        SavedPrefers savedPrefers = userFacade.getSavedPrefers(userId);
        System.out.println("Fetched prefs: " + savedPrefers);
        return ResponseEntity.ok(savedPrefers);
    }

    @PutMapping("/{userId}/prefs")
    public void updateSavedPreferences(
            @PathVariable String userId,
            @RequestBody SavedPrefers savedPrefers
    ){
        userFacade.updateSavedPrefers(userId, savedPrefers);
        System.out.println("SavedPrefers updated for " + userId);
    }

    @PostMapping("/{userId}/plans")
    public ResponseEntity<String> savePlan(
            @PathVariable String userId,
            @RequestBody PlanToSave planToSave
    ){
        int planLength = planToSave.daysToSave().size() - 1;
        LocalDate startDate = LocalDate.parse(planToSave.startDateText());
        LocalDate endDate = startDate.plusDays(planLength);
        List<Plan> savedUserPlans = userFacade.getUserPlans();
        for(Plan savedPlan : savedUserPlans){
            int spLength = savedPlan.plannedDays().size();
            LocalDate spStartDate = savedPlan.plannedDays().get(0).getDate();
            LocalDate spEndDate = savedPlan.plannedDays().get(spLength - 1).getDate();
            if(spStartDate.equals(startDate) && spEndDate.equals(endDate) && savedPlan.plannedDays().size() == planToSave.daysToSave().size()){
                throw new DuplicatePlanException("There is a duplicate plan in plans.");
            }
        }
        for(PlannedDay plannedDay : planToSave.daysToSave()){
            plannedDay.setDate(startDate);
            startDate = startDate.plusDays(1);
        }
        userFacade.saveNewPlan(userId, planToSave.daysToSave());
        System.out.println("Plan saved");
        return ResponseEntity.ok("Saved new plan");
    }
}
