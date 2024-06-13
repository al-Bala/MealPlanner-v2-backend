package com.mealplannerv2.product.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@AllArgsConstructor
@Component
public class UnitsReader {

    private final ObjectMapper objectMapper;

    public Units getUnits(){
        try {
            File file = new File("src/main/resources/units.json");
            return objectMapper.readValue(file, Units.class);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
