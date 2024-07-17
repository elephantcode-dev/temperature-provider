package dev.elephantcode.temperatureprovider.domain;

public record AverageTemperatureQuery(String city) {

    String normalizedCity() {
        return StringNormalizer.normalize(city);
    }

}
