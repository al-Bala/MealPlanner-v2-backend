package com.mealplannerv2.product.unitreader.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mealplannerv2.product.unitreader.error.MultiplierNotFoundException;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.Optional;

@Log4j2
@JsonIgnoreProperties(ignoreUnknown=true)
public record UnitProperties(
        String unit,
        List<Converting> converting
) {

    public Double getMultiplierByOutputUnit(String outputUnit){
        Optional<Double> multiplier = converting.stream()
                .filter(c -> c.output_unit().equals(outputUnit))
                .findFirst()
                .map(Converting::multiplier);
        return multiplier.orElseThrow(() -> new MultiplierNotFoundException("Multiplier not found."));
    }
}
