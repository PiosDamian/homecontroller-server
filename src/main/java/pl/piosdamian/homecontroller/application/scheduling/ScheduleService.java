package pl.piosdamian.homecontroller.application.scheduling;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.piosdamian.homecontroller.application.scheduling.dto.TaskDefinition;
import pl.piosdamian.homecontroller.application.scheduling.storing.ScheduledTasksRepository;
import pl.piosdamian.homecontroller.application.scheduling.taskdefinitionbeans.TaskDefinitionBean;
import pl.piosdamian.homecontroller.application.scheduling.taskdefinitionbeans.TaskDefinitionBeansFactory;

import java.util.Collection;

@Service
@Slf4j
public class ScheduleService {

    private final ScheduledTasksRepository tasksRepository;
    private final TaskSchedulingService schedulingService;
    private final TaskDefinitionBeansFactory beansFactory;

    public ScheduleService(ScheduledTasksRepository tasksRepository, TaskSchedulingService schedulingService, TaskDefinitionBeansFactory beansFactory) {
        this.tasksRepository = tasksRepository;
        this.schedulingService = schedulingService;
        this.beansFactory = beansFactory;

        this.getTasksList().forEach(this::registerTask);
    }


    public void registerTask(final TaskDefinition taskDefinition) {
        final TaskDefinitionBean taskDefinitionBean = this.beansFactory.createTaskDefinitionBean(taskDefinition);
        this.schedulingService.scheduleCronTask(taskDefinition.getName(), taskDefinitionBean, taskDefinitionBean.getTrigger());

        this.tasksRepository.storeTask(taskDefinition);
        log.info("Created task with name: {}, triggered by {}", taskDefinition.getName(), taskDefinitionBean.getTrigger().getClass().getName());
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
