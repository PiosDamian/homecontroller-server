package pl.piosdamian.homecontroller.application.scheduling.taskdefinitionrunner;

import pl.piosdamian.homecontroller.application.scheduling.dto.TaskDefinition;

import java.io.IOException;

public interface TaskDefinitionRunnerFactory {
    TaskDefinitionRunner createTaskDefinitionRunner(final TaskDefinition taskDefinition) throws IOException;
}
