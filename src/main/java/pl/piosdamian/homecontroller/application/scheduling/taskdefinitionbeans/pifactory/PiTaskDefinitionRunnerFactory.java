package pl.piosdamian.homecontroller.application.scheduling.taskdefinitionbeans.pifactory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.piosdamian.homecontroller.application.gpio.GPIOController;
import pl.piosdamian.homecontroller.application.gpio.SensorsController;
import pl.piosdamian.homecontroller.application.scheduling.dto.TaskDefinition;
import pl.piosdamian.homecontroller.application.scheduling.taskdefinitionbeans.TaskDefinitionRunner;
import pl.piosdamian.homecontroller.application.scheduling.taskdefinitionbeans.TaskDefinitionRunnerFactory;
import pl.piosdamian.homecontroller.application.scheduling.taskdefinitionbeans.TriggerFactory;

@Service
@RequiredArgsConstructor
public class PiTaskDefinitionRunnerFactory implements TaskDefinitionRunnerFactory {
    private final GPIOController gpioController;
    private final SensorsController sensorsController;
    private final TriggerFactory triggerFactory;

    public TaskDefinitionRunner createTaskDefinitionRunner(final TaskDefinition taskDefinition) {
        if (taskDefinition.getActionType().equals(TaskDefinition.Type.READ_VALUE)) {
            return new SensorTaskDefinitionRunner(this.sensorsController, taskDefinition, this.triggerFactory.createTrigger(taskDefinition));
        } else if (taskDefinition.getActionType().equals(TaskDefinition.Type.SWITCH)) {
            return new SwitcherTaskDefinitionRunner(this.gpioController, taskDefinition, this.triggerFactory.createTrigger(taskDefinition));
        } else {
            throw new IllegalStateException("Unsupported task definition action type: " + taskDefinition.getActionType());
        }
    }
}
