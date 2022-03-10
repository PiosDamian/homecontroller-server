package pl.piosdamian.homecontroller.application.scheduling.taskdefinitionbeans;

import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.stereotype.Service;
import pl.piosdamian.homecontroller.application.scheduling.dto.TaskDefinition;

import java.util.concurrent.TimeUnit;

@Service
public class TriggerFactoryImpl implements TriggerFactory {

    @Override
    public Trigger createTrigger(TaskDefinition taskDefinition) {
        switch (taskDefinition.getControlType()) {
            case CRON:
                return new CronTrigger(taskDefinition.getExpression());
            case PERIOD:
                final long period = Long.parseLong(taskDefinition.getExpression());
                final PeriodicTrigger periodicTrigger = new PeriodicTrigger(period);
                periodicTrigger.setFixedRate(true);
                periodicTrigger.setInitialDelay(period);
                return periodicTrigger;
            default:
                final PeriodicTrigger defaultTrigger = new PeriodicTrigger(1, TimeUnit.DAYS);
                defaultTrigger.setFixedRate(true);
                return defaultTrigger;
        }
    }
}
