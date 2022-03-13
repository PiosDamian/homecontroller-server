package pl.piosdamian.homecontroller.application.scheduling.taskdefinitionrunner.pifactory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.piosdamian.homecontroller.application.gpio.GPIOController;
import pl.piosdamian.homecontroller.application.gpio.SensorsController;
import pl.piosdamian.homecontroller.application.scheduling.dto.TaskDefinition;
import pl.piosdamian.homecontroller.application.scheduling.taskdefinitionrunner.TaskDefinitionRunner;
import pl.piosdamian.homecontroller.application.scheduling.taskdefinitionrunner.TaskDefinitionRunnerFactory;
import pl.piosdamian.homecontroller.application.scheduling.taskdefinitionrunner.TriggerFactory;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class PiTaskDefinitionRunnerFactory implements TaskDefinitionRunnerFactory {
    private final GPIOController gpioController;
    private final SensorsController sensorsController;
    private final TriggerFactory triggerFactory;

    public TaskDefinitionRunner createTaskDefinitionRunner(final TaskDefinition taskDefinition) throws IOException {
        if (taskDefinition.getActionType().equals("READ_VALUE")) {
            return new SensorTaskDefinitionRunner(this.sensorsController, taskDefinition, this.triggerFactory.createTrigger(taskDefinition.getScheduleDefinition()));
        } else if (taskDefinition.getActionType().equals("SWITCH")) {
            return new SwitcherTaskDefinitionRunner(this.gpioController, taskDefinition, this.triggerFactory.createTrigger(taskDefinition.getScheduleDefinition()));
        } else {
            throw new IllegalStateException("Unsupported task definition action type: " + taskDefinition.getActionType());
        }
    }
}
