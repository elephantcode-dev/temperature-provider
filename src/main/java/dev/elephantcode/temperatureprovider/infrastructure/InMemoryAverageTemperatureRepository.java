package dev.elephantcode.temperatureprovider.infrastructure;

import dev.elephantcode.temperatureprovider.domain.AnnualAverageTemperature;
import dev.elephantcode.temperatureprovider.domain.AverageTemperatureRepository;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
class InMemoryAverageTemperatureRepository implements AverageTemperatureRepository {

    private static final ConcurrentHashMap<String, Set<StoredAnnualAverageTemperature>> STORAGE = new ConcurrentHashMap<>();

    @Override
    public List<AnnualAverageTemperature> findAll(String city) {
        return STORAGE.getOrDefault(city, Set.of())
                .stream()
                .map(StoredAnnualAverageTemperature::to)
                .toList();
    }

    @Override
    public void putAll(String city, List<AnnualAverageTemperature> temperatures) {
        Set<StoredAnnualAverageTemperature> toBeStored = temperatures.stream()
                .map(StoredAnnualAverageTemperature::from)
                .collect(Collectors.toSet());
        STORAGE.compute(city, (key, values) -> {
            if (values == null) {
                return new CopyOnWriteArraySet<>(toBeStored);
            } else {
                toBeStored.forEach(valueToBeStored -> {
                    values.remove(valueToBeStored);
                    values.add(valueToBeStored);
                });
                return values;
            }
        });
    }

    @EqualsAndHashCode(of = "year")
    @Value
    private static class StoredAnnualAverageTemperature {
        String year;
        BigDecimal averageTemperature;

        static StoredAnnualAverageTemperature from(AnnualAverageTemperature source) {
            return new StoredAnnualAverageTemperature(source.year(), source.averageTemperature());
        }

        AnnualAverageTemperature to() {
            return new AnnualAverageTemperature(this.year, this.averageTemperature);
        }
    }
}
