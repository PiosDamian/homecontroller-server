package pl.piosdamian.homecontroller.application.scheduling.storing;

import org.springframework.stereotype.Service;
import pl.piosdamian.homecontroller.application.scheduling.dto.TaskDefinition;

import java.util.*;

@Service
public class MockScheduledTasksRepositoryImpl implements ScheduledTasksRepository {
    private final Map<String, TaskDefinition> tasks = new HashMap<>();

    @Override
    public void storeTask(final TaskDefinition taskDefinition) {
        this.tasks.put(taskDefinition.getName(), taskDefinition);
    }

    @Override
    public Collection<TaskDefinition> listStoredTasks() {
        return new ArrayList<>(this.tasks.values());
    }

    @Override
    public void removeTask(final String name) {
        this.tasks.remove(name);
    }
}
