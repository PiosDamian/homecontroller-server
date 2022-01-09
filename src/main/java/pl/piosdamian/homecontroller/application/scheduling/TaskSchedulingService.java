package pl.piosdamian.homecontroller.application.scheduling;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

@Service
@RequiredArgsConstructor
public class TaskSchedulingService {

    private final TaskScheduler taskScheduler;

    private final Map<String, ScheduledFuture<?>> jobsMap = new HashMap<>();

    public void scheduleTask(final String jobId, final Runnable tasklet, final String cronExpression) {
        System.out.println("Scheduling task with job id: " + jobId + " and cron expression: " + cronExpression);
        ScheduledFuture<?> scheduledTask = taskScheduler.schedule(tasklet, new CronTrigger(cronExpression));
        jobsMap.put(jobId, scheduledTask);

    }

    public void removeScheduledTask(final String jobId) {
        ScheduledFuture<?> scheduledTask = jobsMap.get(jobId);
        if (scheduledTask != null) {
            scheduledTask.cancel(true);
            jobsMap.remove(jobId);
        }
    }
}
