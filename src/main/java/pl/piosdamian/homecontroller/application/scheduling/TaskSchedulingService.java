package pl.piosdamian.homecontroller.application.scheduling;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import pl.piosdamian.homecontroller.application.scheduling.taskdefinitionrunner.TaskDefinitionRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

@Service
@RequiredArgsConstructor
public class TaskSchedulingService {

    private final TaskScheduler taskScheduler;

    private final Map<String, ScheduledFuture<?>> jobsMap = new HashMap<>();

    public void scheduleTask(final TaskDefinitionRunner taskDefinitionRunner) {
        final String jobId = taskDefinitionRunner.getTaskDefinition().getName();
        if (this.jobsMap.containsKey(jobId)) {
            this.removeScheduledTask(jobId);
        }
        jobsMap.put(jobId, taskScheduler.schedule(taskDefinitionRunner, taskDefinitionRunner.getTrigger()));
    }

    public void removeScheduledTask(final String jobId) {
        final ScheduledFuture<?> scheduledTask = jobsMap.get(jobId);
        if (scheduledTask != null) {
            scheduledTask.cancel(false);
            jobsMap.remove(jobId);
        }
    }
}
