package dev.elephantcode.temperatureprovider.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.stereotype.Component;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class AnnualAverageTemperatureCalculator {

    private final AverageTemperatureRepository repository;
    private final TemperatureFileReader temperatureFileReader;

    public void process(File file) {
        var stopWatch = StopWatch.createStarted();
        log.info("Processing file [{}]", file.getName());
        Map<String, YearlyTemperatureCollector> tempStore = new HashMap<>();
        temperatureFileReader.process(file, probe -> compute(tempStore, probe));
        tempStore.forEach((city, collector) ->
                repository.putAll(city, collector.get()));
        stopWatch.stop();
        log.info("Processed file [{}] in {}ms", file.getName(), stopWatch.getTime());
    }

    private void compute(Map<String, YearlyTemperatureCollector> tempStore, TemperatureProbe probe) {
        tempStore.compute(probe.normalizedCity(), (key, collector) -> {
            if (collector == null) {
                return new YearlyTemperatureCollector(probe.normalizedYear(), new TemperatureCollector(probe.temperature()));
            }
            collector.add(probe.normalizedYear(), probe.temperature());
            return collector;
        });
    }

    private static class TemperatureCollector {
        private BigDecimal temperatureSum;
        private long counter;

        TemperatureCollector(BigDecimal temperature) {
            this.temperatureSum = temperature;
            this.counter = 1;
        }

        void add(BigDecimal temperature) {
            temperatureSum = temperatureSum.add(temperature);
            counter++;
        }

        AnnualAverageTemperature get(String year) {
            BigDecimal average = temperatureSum.divide(BigDecimal.valueOf(counter), RoundingMode.HALF_UP);
            return new AnnualAverageTemperature(year, average);
        }
    }

    private static class YearlyTemperatureCollector {

        private final Map<String, TemperatureCollector> collectors = new HashMap<>();

        YearlyTemperatureCollector(String year, TemperatureCollector collector) {
            collectors.put(year, collector);
        }

        void add(String year, BigDecimal temperatureProbe) {
            collectors.compute(year, (key, collector) -> {
                if (collector == null) {
                    return new TemperatureCollector(temperatureProbe);
                }
                collector.add(temperatureProbe);
                return collector;
            });
        }

        List<AnnualAverageTemperature> get() {
            return collectors.entrySet()
                    .stream()
                    .map(entry -> entry.getValue().get(entry.getKey()))
                    .toList();
        }
    }

}
