package dev.elephantcode.temperatureprovider.infrastructure;

import dev.elephantcode.temperatureprovider.domain.TemperatureFileReader;
import dev.elephantcode.temperatureprovider.domain.TemperatureProbe;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.function.Consumer;

@Slf4j
@Component
@RequiredArgsConstructor
class TemperatureFileReaderAdapter implements TemperatureFileReader {

    private final FileLineParser lineParser;

    @Override
    public void process(File file, Consumer<TemperatureProbe> temperatureProbeCallback) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            for (String line; (line = br.readLine()) != null; ) {
                var lineRef = line;
                lineParser.parseLine(line)
                        .ifPresentOrElse(temperatureProbeCallback, () ->
                                log.warn("Line [{}] is invalid. Skipped.", lineRef));
            }
        } catch (Exception ex) {
            log.error("Failed to read file", ex);
        }
    }

}
