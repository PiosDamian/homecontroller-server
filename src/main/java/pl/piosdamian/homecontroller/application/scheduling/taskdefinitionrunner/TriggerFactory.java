package pl.piosdamian.homecontroller.application.scheduling.taskdefinitionrunner;

import org.springframework.scheduling.Trigger;
import pl.piosdamian.homecontroller.application.scheduling.dto.TaskDefinition;

import java.io.IOException;

public interface TriggerFactory {
    Trigger createTrigger(TaskDefinition.ScheduleDefinition scheduleDefinition) throws IOException;
}
