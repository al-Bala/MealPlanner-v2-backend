package com.mealplannerv2.diet;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class DietController {

    private final DietRepository dietRepository;

    @GetMapping("/diets")
    public List<Diet> getAllDiets() {
        return dietRepository.findAll();
    }

}
