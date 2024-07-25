//package com.mealplannerv2.recipe;
//
//import com.mealplannerv2.loginandregister.LoginAndRegisterFacade;
//import com.mealplannerv2.loginandregister.UserRepository;
//import com.mealplannerv2.loginandregister.dto.UserDto;
//import lombok.AllArgsConstructor;
//import org.springframework.stereotype.Component;
//
//@AllArgsConstructor
//@Component
//public class RecipeFacade {
//
//    private final LoginAndRegisterFacade loginAndRegisterFacade;
//    private final UserRepository userRepository;
//
//    public void saveIdOfGeneratedRecipe(PlannedDayDb plannedDay){
//        UserDto user = loginAndRegisterFacade.getAuthenticatedUser();
//
//        user.getPlan().add(plannedDay);
//        userRepository.save(user)
//    }
//
//}
