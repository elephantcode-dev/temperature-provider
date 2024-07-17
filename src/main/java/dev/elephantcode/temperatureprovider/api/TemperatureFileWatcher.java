package dev.elephantcode.temperatureprovider.api;

import dev.elephantcode.temperatureprovider.domain.AnnualAverageTemperatureCalculator;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.*;

@Slf4j
@Component
public class TemperatureFileWatcher {

    private final AnnualAverageTemperatureCalculator calculator;
    private final String fileDirectory;
    private final String fileName;

    public TemperatureFileWatcher(AnnualAverageTemperatureCalculator calculator,
                                  @Value("${app.file.directory}") String fileDirectory,
                                  @Value("${app.file.name}") String fileName) {
        if (StringUtils.isAnyBlank(fileDirectory, fileName)) {
            throw new IllegalStateException("File name nor directory cannot be blank!");
        }
        this.calculator = calculator;
        this.fileDirectory = fileDirectory;
        this.fileName = fileName;
    }

    @PostConstruct
    public void loadWatchedFile() {
        var file = Paths.get(fileDirectory, fileName).toFile();
        initialLoad(file);
        watchFile(file);
    }

    private void initialLoad(File file) {
        log.info("Performing initial file load");
        calculator.process(file);
    }

    private void watchFile(File file) {
        var watcherThread = new Thread(() -> {
            try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
                var dir = Paths.get(fileDirectory);
                dir.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);

                WatchKey key;
                while ((key = observe(watchService)) != null) {
                    for (WatchEvent<?> event : key.pollEvents()) {
                        if (event.context().toString().equals(fileName)) {
                            log.info("File {} changed. Updating...", fileName);
                            calculator.process(file);
                        } else {
                            log.debug("File {} changed. Skipping", event.context());
                        }
                    }
                    key.reset();
                }
            } catch (Exception exception) {
                log.info("File watcher failed. File change won't be noticed.", exception);
            }
        }, "TemperatureFileWatcher");
        watcherThread.setDaemon(true);
        watcherThread.start();
        log.info("Started TemperatureFileWatcher thread: {} as deamon: {}", watcherThread.isAlive(), watcherThread.isDaemon());

    }

    private static WatchKey observe(WatchService watchService) {
        try {
            return watchService.take();
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

}
