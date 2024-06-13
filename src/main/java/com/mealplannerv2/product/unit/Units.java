package com.mealplannerv2.product.unit;

import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.Optional;

@Log4j2
public record Units(
        List<UnitProperties> units_properties
) {

    public Double convertUnit(double amount, String inputUnit, String outputUnit){
        UnitProperties unit = getUnitProperties(inputUnit);
        if(unit == null){
            log.error("Measure not found");
            // TODO: exception
            return null;
        }
        Double multiplier = unit.getMultiplierByOutputUnit(outputUnit);
        if(multiplier == null){
            log.error("Multiplier not found");
            // TODO: exception
            return null;
        }
        return amount * multiplier;
    }

    public UnitProperties getUnitProperties(String unit){
        Optional<UnitProperties> from = units_properties.stream()
                .filter(u -> u.unit().equals(unit))
                .findFirst();
        return from.orElse(null);
    }
}
