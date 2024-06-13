package com.mealplannerv2.product.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UnitsReaderTest {

    @Test
    void should_convert_1000_g_to_1_kg() {
        // given
        UnitsReader unitsReader = new UnitsReader(new ObjectMapper());
        Units units = unitsReader.getUnits();
        // when
        Double convertedUnit = units.convertUnit(1000, "g", "kg");
        // given
        assertThat(convertedUnit).isEqualTo(1.0);
    }
}