package pl.piosdamian.homecontroller.application.service;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.piosdamian.homecontroller.application.gpio.SensorsController;

@Service
@ConditionalOnProperty("schedulers.sensorValueUpdater.enabled")
public class SensorValueUpdaterScheduler {
    private final SensorsController sensorsController;

    public SensorValueUpdaterScheduler(SensorsController sensorsController) {
        this.sensorsController = sensorsController;
    }

    @Scheduled(
            initialDelayString = "${schedulers.sensorValueUpdater.initialDelay}",
            fixedDelayString = "${schedulers.sensorValueUpdater.fixedDelay}"
    )
    public void updateSensorsValues() {
        this.sensorsController.getValues();
    }
}
