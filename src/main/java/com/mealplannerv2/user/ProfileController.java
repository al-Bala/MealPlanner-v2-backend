package com.mealplannerv2.user;

import com.mealplannerv2.auth.AuthFacade;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class ProfileController {

    private final AuthFacade authFacade;
    private final UserRepository userRepository;

    @GetMapping("/users")
    public List<String> users() {
        List<User> all = userRepository.findAll();
        return all.stream()
                .map(User::getUsername)
                .toList();
    }

//    @GetMapping("/profile/{id}")
//    public String profile(@PathVariable int id) {
//        UserDto authenticatedUser = loginAndRegisterFacade.getAuthenticatedUser();
//    }

}
