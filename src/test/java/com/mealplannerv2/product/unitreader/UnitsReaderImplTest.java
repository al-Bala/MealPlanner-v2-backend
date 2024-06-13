package com.mealplannerv2.product.unitreader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mealplannerv2.product.unitreader.dto.Converting;
import com.mealplannerv2.product.unitreader.dto.UnitProperties;
import com.mealplannerv2.product.unitreader.dto.Units;
import com.mealplannerv2.product.unitreader.error.MultiplierNotFoundException;
import com.mealplannerv2.product.unitreader.error.UnitsPropertiesNotFoundException;
import nl.altindag.log.LogCaptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UnitsReaderImplTest {

    UnitsReaderImpl unitsReaderImpl;
    Units units;
    LogCaptor logCaptor;

    @BeforeEach
    void setUp() {
        unitsReaderImpl = new UnitsReaderImpl(new ObjectMapper());
        units = unitsReaderImpl.getUnits();
        logCaptor = LogCaptor.forClass(Units.class);
    }

    @Test
    void should_convert_1000_g_to_1_kg() {
        // when
        Double convertedUnit = units.convertUnit(1000, "g", "kg");
        // then
        assertThat(convertedUnit).isEqualTo(1.0);
    }

    @Test
    void should_return_null_when_inputUnit_not_exist() {
        // when
        Double convertedUnit = units.convertUnit(1000, "non-existing unit", "g");
        List<String> errorLogs = logCaptor.getErrorLogs();
        // then
        assertThat(convertedUnit).isEqualTo(null);
        assertThat(errorLogs).containsExactly("UnitProperties not found.");
    }

    @Test
    void should_return_null_when_outputUnit_not_exist() {
        // when
        Double convertedUnit = units.convertUnit(1000, "g", "non-existing unit");
        List<String> errorLogs = logCaptor.getErrorLogs();
        // then
        assertThat(convertedUnit).isEqualTo(null);
        assertThat(errorLogs).containsExactly("Multiplier not found.");
    }

    @Test
    void should_throw_exception_when_unit_for_UnitProperties_not_exist() {
        // when & then
        assertThatThrownBy(() -> units.getUnitProperties("non-existing unit"))
                .isInstanceOf(UnitsPropertiesNotFoundException.class)
                .hasMessage("UnitProperties not found.");
    }

    @Test
    void should_throw_exception_when_unit_for_Multiplier_not_exist() {
        // given
        UnitProperties unitProperties = new UnitProperties("kg" , List.of(new Converting("g", 1000)));
        // when & then
        assertThatThrownBy(() -> unitProperties.getMultiplierByOutputUnit("non-existing unit"))
                .isInstanceOf(MultiplierNotFoundException.class)
                .hasMessage("Multiplier not found.");
    }
}