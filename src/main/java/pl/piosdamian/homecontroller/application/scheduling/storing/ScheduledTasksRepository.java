package pl.piosdamian.homecontroller.application.scheduling.storing;

import pl.piosdamian.homecontroller.application.scheduling.dto.TaskDefinition;

import java.util.Collection;

public interface ScheduledTasksRepository {
    void storeTask(final TaskDefinition taskDefinition);
    Collection<TaskDefinition> listStoredTasks();
    void removeTask(final String name);
}
