package dev.elephantcode.temperatureprovider.domain;

import java.util.List;

public interface AverageTemperatureRepository {

    List<AnnualAverageTemperature> findAll(String city);

    void putAll(String city, List<AnnualAverageTemperature> temperature);

}
