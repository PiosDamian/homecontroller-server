package pl.piosdamian.homecontroller.application.scheduling.taskdefinitionbeans;

import lombok.extern.slf4j.Slf4j;
import pl.piosdamian.homecontroller.application.gpio.GPIOController;
import pl.piosdamian.homecontroller.application.gpio.SensorsController;
import pl.piosdamian.homecontroller.application.scheduling.dto.TaskDefinition;

@Slf4j
public class TaskDefinitionBean implements Runnable {

    private final GPIOController gpioController;
    private final SensorsController sensorsController;

    private final TaskDefinition taskDefinition;

    public TaskDefinitionBean(GPIOController gpioController, SensorsController sensorsController, TaskDefinition taskDefinition) {
        if(!this.checkPresenceOfAddress()) {
            throw new IllegalArgumentException("Address is not registered");
        }

        this.gpioController = gpioController;
        this.sensorsController = sensorsController;
        this.taskDefinition = taskDefinition;
    }

    @Override
    public void run() {
        switch (this.taskDefinition.getActionType()) {
            case SWITCH:
                this.blink();
                break;
            case READ_VALUE:
                this.readValue();
                break;
            default:
                log.info("Unknown operation: {}", this.taskDefinition.getActionType());
        }
    }

    private boolean checkPresenceOfAddress() {
                final String address = this.taskDefinition.getData().get("address");
        switch (this.taskDefinition.getActionType()) {
            case SWITCH:
                return this.gpioController.getReservedPins().contains(Integer.getInteger(address));
            case READ_VALUE:
                return this.sensorsController.getSensors().stream().filter(sensor -> sensor.getAddress().equals(address)).count() == 1;
            default:
                throw new IllegalArgumentException("Unknown operation: " + this.taskDefinition.getActionType() );
        }
    }

    private void blink() {
        this.gpioController.blink(Integer.getInteger(this.taskDefinition.getData().get("address")));
    }

    private void readValue() {
        final String address = this.getTaskDefinition().getData().get("address");
        this.sensorsController.getSensors().stream().filter(sensor -> sensor.getAddress().equals(address)).findFirst().ifPresentOrElse(
                sensor -> log.info("value from sensor {}: {}", sensor.getName(), sensor.getValue()),
                () -> log.warn("Sensor with address: {} not found", address));
    }

    public TaskDefinition getTaskDefinition() {
        return taskDefinition;
    }
}
