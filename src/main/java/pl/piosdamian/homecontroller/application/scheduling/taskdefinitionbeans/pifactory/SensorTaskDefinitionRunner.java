package pl.piosdamian.homecontroller.application.scheduling.taskdefinitionbeans.pifactory;

import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.Trigger;
import pl.piosdamian.homecontroller.application.gpio.SensorsController;
import pl.piosdamian.homecontroller.application.scheduling.dto.TaskDefinition;
import pl.piosdamian.homecontroller.application.scheduling.taskdefinitionbeans.TaskDefinitionRunner;

@Slf4j
class SensorTaskDefinitionRunner implements TaskDefinitionRunner {

    private final SensorsController sensorsController;

    @Getter
    private final TaskDefinition taskDefinition;
    @Getter
    private final Trigger trigger;

    SensorTaskDefinitionRunner(final SensorsController sensorsController, @NonNull final TaskDefinition taskDefinition, final Trigger trigger) {
        this.taskDefinition = taskDefinition;
        this.sensorsController = sensorsController;
        if (!this.checkPresenceOfAddress()) {
            throw new IllegalArgumentException("Address is not registered");
        }

        this.trigger = trigger;
    }

    @Override
    public void run() {
        final String address = this.getTaskDefinition().getData().get("address");
        this.sensorsController.getSensors().stream().filter(sensor -> sensor.getAddress().equals(address)).findFirst().ifPresentOrElse(
                sensor -> log.info("value from sensor {}: {}", sensor.getName(), sensor.getValue()),
                () -> log.warn("Sensor with address: {} not found", address));
    }

    private boolean checkPresenceOfAddress() {
        final String address = this.taskDefinition.getData().get("address");
        return this.sensorsController.getSensors().stream().filter(sensor -> sensor.getAddress().equals(address)).count() == 1;
    }

    @Override
    public String getName() {
        return this.getTaskDefinition().getName();
    }
}
