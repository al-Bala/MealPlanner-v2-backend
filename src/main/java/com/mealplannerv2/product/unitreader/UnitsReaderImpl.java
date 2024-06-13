package com.mealplannerv2.product.unitreader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mealplannerv2.product.unitreader.dto.Units;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@AllArgsConstructor
@Component
public class UnitsReaderImpl implements UnitReader {

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
