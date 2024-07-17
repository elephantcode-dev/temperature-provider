package dev.elephantcode.temperatureprovider.domain;

import lombok.NonNull;

import java.math.BigDecimal;

public record TemperatureProbe(@NonNull String city,
                               @NonNull String year,
                               @NonNull BigDecimal temperature) {

    String normalizedCity() {
        return StringNormalizer.normalize(city);
    }

    String normalizedYear() {
        return StringNormalizer.normalize(year);
    }
}
