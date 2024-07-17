package dev.elephantcode.temperatureprovider.domain;

import java.io.File;
import java.util.function.Consumer;

public interface TemperatureFileReader {

    void process(File file, Consumer<TemperatureProbe> temperatureProbeConsumer);

}
