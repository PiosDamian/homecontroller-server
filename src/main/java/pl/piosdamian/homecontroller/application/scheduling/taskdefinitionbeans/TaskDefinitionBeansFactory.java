package pl.piosdamian.homecontroller.application.scheduling.taskdefinitionbeans;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.piosdamian.homecontroller.application.gpio.GPIOController;
import pl.piosdamian.homecontroller.application.gpio.SensorsController;
import pl.piosdamian.homecontroller.application.scheduling.dto.TaskDefinition;

@Service
@RequiredArgsConstructor
public class TaskDefinitionBeansFactory {
    private final GPIOController gpioController;
    private final SensorsController sensorsController;

    public TaskDefinitionBean createTaskDefinitionBean(final TaskDefinition taskDefinition) {
        return new TaskDefinitionBean(this.gpioController, this.sensorsController, taskDefinition);
    }
}
