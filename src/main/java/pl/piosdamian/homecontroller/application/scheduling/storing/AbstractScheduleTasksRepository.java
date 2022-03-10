package pl.piosdamian.homecontroller.application.scheduling.storing;

import lombok.extern.slf4j.Slf4j;
import pl.piosdamian.homecontroller.application.scheduling.dto.TaskDefinition;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
abstract class AbstractScheduleTasksRepository implements ScheduledTasksRepository {
    protected final Map<String, TaskDefinition> tasks;

    protected AbstractScheduleTasksRepository() {
        Map<String, TaskDefinition> config = new HashMap<>();
        try {
            config = this.readConfiguration();
        } catch (IOException e) {
            log.warn("Cannot read schedule configuration: {}", e.getMessage());
        } finally {
            this.tasks = config;
        }
    }

    @Override
    public void storeTask(final TaskDefinition taskDefinition) {
        this.tasks.put(taskDefinition.getName(), taskDefinition);
        this.storeSchedulers();
    }

    @Override
    public Collection<TaskDefinition> listStoredTasks() {
        return new ArrayList<>(this.tasks.values());
    }

    @Override
    public void removeTask(final String name) {
        this.tasks.remove(name);
        this.storeSchedulers();
    }

    protected abstract void storeSchedulers();

    protected abstract Map<String, TaskDefinition> readConfiguration() throws IOException;
}
