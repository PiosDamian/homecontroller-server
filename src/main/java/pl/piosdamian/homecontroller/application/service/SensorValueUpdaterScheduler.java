package pl.piosdamian.homecontroller.application.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.piosdamian.homecontroller.application.gpio.SensorsController;

@Service
@ConditionalOnProperty("schedulers.sensorValueUpdater.enabled")
@Slf4j
public class SensorValueUpdaterScheduler {
    private final SensorsController sensorsController;

    public SensorValueUpdaterScheduler(final SensorsController sensorsController) {
        this.sensorsController = sensorsController;
    }

    @Scheduled(
            initialDelayString = "${schedulers.sensorValueUpdater.initialDelay}",
            fixedDelayString = "${schedulers.sensorValueUpdater.fixedDelay}"
    )
    public void updateSensorsValues() {
        log.debug("Refreshing values");
        this.sensorsController.getValues();
    }
}
