package dev.elephantcode.temperatureprovider.domain;

import java.math.BigDecimal;

public record AnnualAverageTemperature(String year,
                                       BigDecimal averageTemperature) {
}
