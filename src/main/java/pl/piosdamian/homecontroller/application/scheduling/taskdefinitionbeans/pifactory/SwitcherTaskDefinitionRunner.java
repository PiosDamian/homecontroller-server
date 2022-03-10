package pl.piosdamian.homecontroller.application.scheduling.taskdefinitionbeans.pifactory;

import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.Trigger;
import pl.piosdamian.homecontroller.application.gpio.GPIOController;
import pl.piosdamian.homecontroller.application.scheduling.dto.TaskDefinition;
import pl.piosdamian.homecontroller.application.scheduling.taskdefinitionbeans.TaskDefinitionRunner;

@Slf4j
class SwitcherTaskDefinitionRunner implements TaskDefinitionRunner {
    private final GPIOController gpioController;

    @Getter
    private final TaskDefinition taskDefinition;
    @Getter
    private final Trigger trigger;

    SwitcherTaskDefinitionRunner(final GPIOController gpioController, @NonNull final TaskDefinition taskDefinition, final Trigger trigger) {
        this.taskDefinition = taskDefinition;
        this.gpioController = gpioController;
        if (!this.checkPresenceOfAddress()) {
            throw new IllegalArgumentException("Address is not registered");
        }
        this.trigger = trigger;
    }

    @Override
    public void run() {
        this.gpioController.blink(Integer.parseInt(this.taskDefinition.getData().get("address")));
    }

    private boolean checkPresenceOfAddress() {
        final String address = this.taskDefinition.getData().get("address");
        return this.gpioController.getReservedPins().contains(Integer.parseInt(address));

    }

    @Override
    public String getName() {
        return this.getTaskDefinition().getName();
    }
}
