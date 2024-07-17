package dev.elephantcode.temperatureprovider.infrastructure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class TemperatureFileReaderConfiguration {

    @Bean
    CsvLineParser csvLineParser() {
        return new CsvLineParser();
    }

    @Bean
    TemperatureFileReaderAdapter temperatureFileReaderAdapter(FileLineParser fileLineParser) {
        return new TemperatureFileReaderAdapter(fileLineParser);
    }
}
