package dev.elephantcode.temperatureprovider.api;

import dev.elephantcode.temperatureprovider.domain.AnnualAverageTemperatureQueryHandler;
import dev.elephantcode.temperatureprovider.domain.AverageTemperatureQuery;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/cities/{city}/temperature/average")
@RequiredArgsConstructor
@Validated
public class CitiesAverageTemperatureController {


    private final AnnualAverageTemperatureQueryHandler annualAverageTemperatureQueryHandler;

    /**
     * Retrieves the annual average temperature of a given city.
     *
     * @param city The name of the city. 100 signs seems to be reasonable limit for the city name.
     *             Validation added to avoid sending whole poems as request parameter.
     * @return A list of AnnualAverageTemperature objects containing the year and average temperature.
     */
    @GetMapping
    public List<AnnualAverageTemperatureDto> getCityAnnualAverageTemperature(
            @NotBlank @Size(min = 1, max = 100) @PathVariable("city") String city
    ) {
        return annualAverageTemperatureQueryHandler.handle(new AverageTemperatureQuery(city))
                .stream()
                .map(AnnualAverageTemperatureDto::from)
                .toList();
    }

}
