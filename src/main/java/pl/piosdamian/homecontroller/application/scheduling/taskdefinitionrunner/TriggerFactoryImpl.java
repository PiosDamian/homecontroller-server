package pl.piosdamian.homecontroller.application.scheduling.taskdefinitionrunner;

import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.stereotype.Service;
import pl.piosdamian.homecontroller.application.scheduling.dto.TaskDefinition;
import pl.piosdamian.homecontroller.application.scheduling.triggers.PeriodWithStartDateTrigger;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
public class TriggerFactoryImpl implements TriggerFactory {

    @Override
    public Trigger createTrigger(TaskDefinition.ScheduleDefinition scheduleDefinition) throws IOException {
        switch (scheduleDefinition.getScheduleType()) {
            case CRON:
                return new CronTrigger(scheduleDefinition.getExpression());
            case PERIOD:
                final long period = Long.parseLong(scheduleDefinition.getExpression());
                final PeriodicTrigger periodicTrigger = new PeriodicTrigger(period);
                periodicTrigger.setFixedRate(true);
                periodicTrigger.setInitialDelay(period);
                return periodicTrigger;
            case PERIOD_WITH_START:
                return new PeriodWithStartDateTrigger(scheduleDefinition.getExpression());
            default:
                final PeriodicTrigger defaultTrigger = new PeriodicTrigger(1, TimeUnit.DAYS);
                defaultTrigger.setFixedRate(true);
                return defaultTrigger;
        }
    }
}
