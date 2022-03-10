package pl.piosdamian.homecontroller.application.scheduling;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.piosdamian.homecontroller.application.scheduling.dto.TaskDefinition;
import pl.piosdamian.homecontroller.application.scheduling.storing.ScheduledTasksRepository;
import pl.piosdamian.homecontroller.application.scheduling.taskdefinitionbeans.TaskDefinitionRunner;
import pl.piosdamian.homecontroller.application.scheduling.taskdefinitionbeans.TaskDefinitionRunnerFactory;
import pl.piosdamian.homecontroller.application.scheduling.taskdefinitionbeans.pifactory.PiTaskDefinitionRunnerFactory;

import java.util.Collection;

@Service
@Slf4j
public class ScheduleService {

    private final ScheduledTasksRepository tasksRepository;
    private final TaskSchedulingService schedulingService;
    private final TaskDefinitionRunnerFactory beansFactory;

    public ScheduleService(final ScheduledTasksRepository tasksRepository, final TaskSchedulingService schedulingService, final PiTaskDefinitionRunnerFactory beansFactory) {
        this.tasksRepository = tasksRepository;
        this.schedulingService = schedulingService;
        this.beansFactory = beansFactory;

        this.getTasksList().forEach(this::registerTask);
    }


    public void registerTask(final TaskDefinition taskDefinition) {
        final TaskDefinitionRunner taskDefinitionRunner = this.beansFactory.createTaskDefinitionRunner(taskDefinition);
        this.schedulingService.scheduleCronTask(taskDefinitionRunner);

        this.tasksRepository.storeTask(taskDefinition);
        log.info("Created task with name: {}, triggered by {}", taskDefinition.getName(), taskDefinitionRunner.getTrigger().getClass().getName());
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
