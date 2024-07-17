package dev.elephantcode.temperatureprovider.domain;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AnnualAverageTemperatureQueryHandler {

    private final AverageTemperatureRepository repository;

    public List<AnnualAverageTemperature> handle(AverageTemperatureQuery query) {
        log.info("Searching for data for city {}", query.normalizedCity());
        return repository.findAll(query.normalizedCity());
    }

}
