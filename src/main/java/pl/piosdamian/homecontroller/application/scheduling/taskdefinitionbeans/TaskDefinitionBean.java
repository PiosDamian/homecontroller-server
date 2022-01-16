package pl.piosdamian.homecontroller.application.scheduling.taskdefinitionbeans;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.PeriodicTrigger;
import pl.piosdamian.homecontroller.application.gpio.GPIOController;
import pl.piosdamian.homecontroller.application.gpio.SensorsController;
import pl.piosdamian.homecontroller.application.scheduling.dto.TaskDefinition;

import java.util.concurrent.TimeUnit;

@Slf4j
public class TaskDefinitionBean implements Runnable {

    private final GPIOController gpioController;
    private final SensorsController sensorsController;

    @Getter
    private final TaskDefinition taskDefinition;
    @Getter
    private final Trigger trigger;

    public TaskDefinitionBean(final GPIOController gpioController, final SensorsController sensorsController, final TaskDefinition taskDefinition) {
        this.taskDefinition = taskDefinition;
        this.gpioController = gpioController;
        this.sensorsController = sensorsController;
        if (!this.checkPresenceOfAddress()) {
            throw new IllegalArgumentException("Address is not registered");
        }

        switch (taskDefinition.getControlType()) {
            case CRON:
                this.trigger = new CronTrigger(taskDefinition.getExpression());
                break;
            case PERIOD:
                final long period = Long.parseLong(taskDefinition.getExpression());
                final PeriodicTrigger periodicTrigger = new PeriodicTrigger(period);
                periodicTrigger.setFixedRate(true);
                periodicTrigger.setInitialDelay(period);
                this.trigger = periodicTrigger;
                break;
            default:
                final PeriodicTrigger defaultTrigger = new PeriodicTrigger(1, TimeUnit.DAYS);
                defaultTrigger.setFixedRate(true);
                this.trigger = defaultTrigger;
        }
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
                return this.gpioController.getReservedPins().contains(Integer.parseInt(address));
            case READ_VALUE:
                return this.sensorsController.getSensors().stream().filter(sensor -> sensor.getAddress().equals(address)).count() == 1;
            default:
                throw new IllegalArgumentException("Unknown operation: " + this.taskDefinition.getActionType());
        }
    }

    private void blink() {
        this.gpioController.blink(Integer.parseInt(this.taskDefinition.getData().get("address")));
    }

    private void readValue() {
        final String address = this.getTaskDefinition().getData().get("address");
        this.sensorsController.getSensors().stream().filter(sensor -> sensor.getAddress().equals(address)).findFirst().ifPresentOrElse(
                sensor -> log.info("value from sensor {}: {}", sensor.getName(), sensor.getValue()),
                () -> log.warn("Sensor with address: {} not found", address));
    }
}
