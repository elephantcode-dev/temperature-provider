package dev.elephantcode.temperatureprovider.infrastructure;

import dev.elephantcode.temperatureprovider.domain.TemperatureProbe;

import java.util.Optional;

interface FileLineParser {

    Optional<TemperatureProbe> parseLine(String fileLine);

}
