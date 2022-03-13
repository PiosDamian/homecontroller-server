package pl.piosdamian.homecontroller.application.scheduling;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.piosdamian.homecontroller.application.scheduling.dto.TaskDefinition;
import pl.piosdamian.homecontroller.application.scheduling.storing.ScheduledTasksRepository;
import pl.piosdamian.homecontroller.application.scheduling.taskdefinitionrunner.TaskDefinitionRunner;
import pl.piosdamian.homecontroller.application.scheduling.taskdefinitionrunner.TaskDefinitionRunnerFactory;
import pl.piosdamian.homecontroller.application.scheduling.taskdefinitionrunner.pifactory.PiTaskDefinitionRunnerFactory;

import java.io.IOException;
import java.util.Collection;

@Service
@Slf4j
public class ScheduleService {

    private final ScheduledTasksRepository tasksRepository;
    private final TaskSchedulingService schedulingService;
    private final TaskDefinitionRunnerFactory definitionRunnerFactory;

    public ScheduleService(final ScheduledTasksRepository tasksRepository, final TaskSchedulingService schedulingService, final PiTaskDefinitionRunnerFactory definitionRunnerFactory) {
        this.tasksRepository = tasksRepository;
        this.schedulingService = schedulingService;
        this.definitionRunnerFactory = definitionRunnerFactory;
        this.getTasksList().forEach(this::registerTask);
    }


    public void registerTask(final TaskDefinition taskDefinition) {
        try {
            final TaskDefinitionRunner taskDefinitionRunner = this.definitionRunnerFactory.createTaskDefinitionRunner(taskDefinition);
            this.schedulingService.scheduleTask(taskDefinitionRunner);

            this.tasksRepository.storeTask(taskDefinition);
            log.info("Created task with name: {}, triggered by {}", taskDefinition.getName(), taskDefinitionRunner.getTrigger().getClass().getName());
        } catch (IOException e) {
            log.error("Problem with registering stored tasks: {}, for: {}", e.getMessage(), taskDefinition);
            throw new RuntimeException(e);
        }
    }

    public Collection<TaskDefinition> getTasksList() {
        return this.tasksRepository.listStoredTasks();
    }

    public void removeTask(final String taskName) {
        log.info("Removed task with name: {}", taskName);
        this.schedulingService.removeScheduledTask(taskName);
        this.tasksRepository.removeTask(taskName);
    }
}
