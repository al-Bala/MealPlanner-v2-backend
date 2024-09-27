package com.mealplannerv2.user;

import com.mealplannerv2.auth.dto.UserDto;
import com.mealplannerv2.plangenerator.infrastructure.controller.dto.SavedPrefers;
import com.mealplannerv2.recipe.DayPlan;
import com.mealplannerv2.recipe.Plan;
import com.mealplannerv2.user.dto.Profile;
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
        return ResponseEntity.ok(savedPrefers);
    }

    @PutMapping("/{userId}/prefs")
    public void updateSavedPreferences(
            @PathVariable String userId,
            @RequestBody SavedPrefers savedPrefers
    ){
        userFacade.updateSavedPrefers(userId, savedPrefers);
    }

    @PostMapping("/{userId}/plans")
    public ResponseEntity<String> savePlan(
            @PathVariable String userId,
            @RequestBody TempPlan tempPlan
    ){
        int planLength = tempPlan.tempDays().size() - 1;
        LocalDate startDate = LocalDate.parse(tempPlan.startDateText());
        LocalDate endDate = startDate.plusDays(planLength);
        List<Plan> savedUserPlans = userFacade.getUserPlans();
        for(Plan savedPlan : savedUserPlans){
            int spLength = savedPlan.days().size();
            LocalDate spStartDate = savedPlan.days().get(0).getDate();
            LocalDate spEndDate = savedPlan.days().get(spLength - 1).getDate();
            if(spStartDate.equals(startDate) && spEndDate.equals(endDate) && savedPlan.days().size() == tempPlan.tempDays().size()){
                throw new DuplicatePlanException("There is a duplicate plan in plans.");
            }
        }
        for(DayPlan dayPlan : tempPlan.tempDays()){
            dayPlan.setDate(startDate);
            startDate = startDate.plusDays(1);
        }
        userFacade.saveNewPlan(userId, tempPlan.tempDays());
        System.out.println("Plan saved");
        return ResponseEntity.ok("Saved new plan");
    }
}
