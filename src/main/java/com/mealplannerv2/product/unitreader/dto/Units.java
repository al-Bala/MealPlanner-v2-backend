package com.mealplannerv2.product.unitreader.dto;

import com.mealplannerv2.product.unitreader.error.MultiplierNotFoundException;
import com.mealplannerv2.product.unitreader.error.UnitsPropertiesNotFoundException;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.Optional;

@Log4j2
public record Units(
        List<UnitProperties> units_properties
) {

    public Double convertUnit(double amount, String inputUnit, String outputUnit){
        try{
            UnitProperties unit= getUnitProperties(inputUnit);
            Double multiplier = unit.getMultiplierByOutputUnit(outputUnit);
            return amount * multiplier;
        } catch (UnitsPropertiesNotFoundException | MultiplierNotFoundException exception){
            log.error(exception.getMessage());
        }
        return null;
    }

    public UnitProperties getUnitProperties(String unit){
        Optional<UnitProperties> from = units_properties.stream()
                .filter(u -> u.unit().equals(unit))
                .findFirst();
        return from.orElseThrow(() -> new UnitsPropertiesNotFoundException("UnitProperties not found."));
    }
}
