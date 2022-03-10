package pl.piosdamian.homecontroller.application.scheduling.taskdefinitionbeans;

import pl.piosdamian.homecontroller.application.scheduling.dto.TaskDefinition;

public interface TaskDefinitionRunnerFactory {
    TaskDefinitionRunner createTaskDefinitionRunner(final TaskDefinition taskDefinition);
}
