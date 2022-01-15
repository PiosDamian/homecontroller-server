package pl.piosdamian.homecontroller.application.scheduling;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

@Service
@RequiredArgsConstructor
public class TaskSchedulingService {

    private final TaskScheduler taskScheduler;

    private final Map<String, ScheduledFuture<?>> jobsMap = new HashMap<>();

    public void scheduleCronTask(final String jobId, final Runnable tasklet, final Trigger trigger) {
        jobsMap.put(jobId, taskScheduler.schedule(tasklet, trigger));
    }

    public void removeScheduledTask(final String jobId) {
        ScheduledFuture<?> scheduledTask = jobsMap.get(jobId);
        if (scheduledTask != null) {
            scheduledTask.cancel(false);
            jobsMap.remove(jobId);
        }
    }
}
