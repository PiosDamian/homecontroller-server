package pl.piosdamian.homecontroller.application.scheduling.taskdefinitionbeans;

import org.springframework.scheduling.Trigger;
import pl.piosdamian.homecontroller.application.scheduling.dto.TaskDefinition;

public interface TaskDefinitionRunner extends Runnable {
    TaskDefinition getTaskDefinition();

    Trigger getTrigger();

    String getName();
}
