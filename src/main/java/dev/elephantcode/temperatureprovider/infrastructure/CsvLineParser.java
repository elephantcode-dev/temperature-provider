package dev.elephantcode.temperatureprovider.infrastructure;

import dev.elephantcode.temperatureprovider.domain.TemperatureProbe;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Optional;

@Slf4j
class CsvLineParser implements FileLineParser {

    private static final String CSV_SEPARATOR = ";"; // fun fact - that's semicolon, so the file should be an .SSV - semicolon separated values
    private static final String DATE_SEPARATOR = "-";

    @Override
    public Optional<TemperatureProbe> parseLine(String fileLine) {
        if (fileLine != null && !fileLine.isBlank()) {
            String[] dividedLine = fileLine.trim().split(CSV_SEPARATOR);
            if (dividedLine.length == 3) {
                try {
                    return parse(dividedLine);
                } catch (Exception exception) {
                    log.warn("Failed to parse line [{}]", fileLine, exception);
                }
            }
        }
        return Optional.empty();
    }

    private static Optional<TemperatureProbe> parse(String[] dividedLine) {
        var city = dividedLine[0];
        var year = dividedLine[1].split(DATE_SEPARATOR)[0]; //To consider - parsing to date type for validation.
        var temperatureProbe = dividedLine[2];
        if (StringUtils.isNoneBlank(city, year, temperatureProbe)) {
            return Optional.of(new TemperatureProbe(city, year, new BigDecimal(temperatureProbe)));
        }
        return Optional.empty();
    }
}
