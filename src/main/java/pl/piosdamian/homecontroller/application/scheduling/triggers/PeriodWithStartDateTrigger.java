package pl.piosdamian.homecontroller.application.scheduling.triggers;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Data;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import pl.piosdamian.homecontroller.application.utils.Mapper;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class PeriodWithStartDateTrigger implements Trigger {

    private final StartHour startHour;
    private final long period;

    public PeriodWithStartDateTrigger(final StartHour startDate, final long period) {
        this(startDate, period, null);
    }

    public PeriodWithStartDateTrigger(final StartHour startDate, final long period, final @Nullable TimeUnit timeUnit) {
        this.startHour = startDate;
        TimeUnit timeUnit1 = timeUnit == null ? TimeUnit.MILLISECONDS : timeUnit;
        this.period = timeUnit1.toMillis(period);
    }

    public PeriodWithStartDateTrigger(final String definition) throws JsonProcessingException {
        this(Mapper.JSON_MAPPER.readValue(definition, PeriodWithStartDateDefinition.class));
    }

    private PeriodWithStartDateTrigger(final PeriodWithStartDateDefinition definition) {
        this(definition.getStart(), definition.getPeriod(), definition.getTimeUnit());
    }

    @Override
    public Date nextExecutionTime(TriggerContext triggerContext) {
        final Date lastActualExecutionTime = triggerContext.lastScheduledExecutionTime();
        if (lastActualExecutionTime == null) {

            LocalDateTime time = LocalDateTime.now()
                    .withHour(this.startHour.getHour())
                    .withMinute(this.startHour.getMinute())
                    .withSecond(this.startHour.getSecond())
                    .withNano(0);

            if (time.isBefore(LocalDateTime.now())) {
                time = time.withDayOfYear(time.getDayOfYear() + 1);
            }

            return Date.from(time.atZone(ZoneId.systemDefault()).toInstant());
        } else {
            return Date.from(lastActualExecutionTime.toInstant().plus(period, ChronoUnit.MILLIS));
        }
    }

    @Data
    private static class PeriodWithStartDateDefinition {
        private StartHour start;
        private long period;
        private TimeUnit timeUnit;
    }

    @Data
    public static class StartHour {
        private byte hour;
        private byte minute;
        private Byte second;

        public byte getSecond() {
            return this.second == null ? 0 : this.second;
        }
    }
}
