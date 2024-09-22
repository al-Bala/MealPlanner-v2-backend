package com.mealplannerv2.user;

import com.mealplannerv2.user.dto.Profile;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    private final UserFacade userFacade;

    @GetMapping("/users")
    public List<String> users() {
        List<User> all = userRepository.findAll();
        return all.stream()
                .map(User::getUsername)
                .toList();
    }

    @GetMapping("/profile/{username}")
    public ResponseEntity<Profile> showProfile(@PathVariable String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BadCredentialsException("Username not found"));

        Profile profile = Profile.builder()
                .username(user.getUsername())
                .history(user.getHistory())
                .build();
        return ResponseEntity.ok(profile);
    }
}
