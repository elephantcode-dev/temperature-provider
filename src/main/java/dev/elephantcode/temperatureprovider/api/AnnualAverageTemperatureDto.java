package dev.elephantcode.temperatureprovider.api;

import dev.elephantcode.temperatureprovider.domain.AnnualAverageTemperature;

import java.math.BigDecimal;

public record AnnualAverageTemperatureDto(String year,
                                          BigDecimal averageTemperature) {

    static AnnualAverageTemperatureDto from(AnnualAverageTemperature source) {
        return new AnnualAverageTemperatureDto(source.year(), source.averageTemperature());
    }
}
