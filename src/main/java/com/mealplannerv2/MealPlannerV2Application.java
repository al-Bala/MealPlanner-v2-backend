package com.mealplannerv2;

import com.mealplannerv2.infrastructure.security.jwt.JwtConfigurationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(value = {JwtConfigurationProperties.class})
public class MealPlannerV2Application {

    public static void main(String[] args) {
        SpringApplication.run(MealPlannerV2Application.class, args);
    }

}
