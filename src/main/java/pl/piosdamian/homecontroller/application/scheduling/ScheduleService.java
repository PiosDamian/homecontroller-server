package pl.piosdamian.homecontroller.application.scheduling;

import org.springframework.stereotype.Service;
import pl.piosdamian.homecontroller.application.scheduling.dto.TaskDefinition;
import pl.piosdamian.homecontroller.application.scheduling.storing.ScheduledTasksRepository;
import pl.piosdamian.homecontroller.application.scheduling.taskdefinitionbeans.TaskDefinitionBean;
import pl.piosdamian.homecontroller.application.scheduling.taskdefinitionbeans.TaskDefinitionBeansFactory;

import java.util.Collection;

@Service
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


    public void registerTask(final TaskDefinition  taskDefinition) {
        final TaskDefinitionBean taskDefinitionBean = this.beansFactory.createTaskDefinitionBean(taskDefinition);

        this.schedulingService.scheduleTask(taskDefinition.getName(), taskDefinitionBean, taskDefinition.getCronExpression());

        this.tasksRepository.storeTask(taskDefinition);
    }

    public Collection<TaskDefinition> getTasksList() {
        return this.tasksRepository.listStoredTasks();
    }

    public void removeTask(final String taskName) {
        this.schedulingService.removeScheduledTask(taskName);
        this.tasksRepository.removeTask(taskName);
    }
}
