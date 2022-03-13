package pl.piosdamian.homecontroller.application.scheduling.dto;

import lombok.Data;
import lombok.ToString;

import java.util.Map;

@Data
@ToString
public class TaskDefinition {
    private final String name;
    private final ScheduleDefinition scheduleDefinition;
    private final String actionType;
    private final Map<String, String> data;

    public enum ScheduleType {
        CRON, PERIOD, PERIOD_WITH_START
    }

    @Data
    public static class ScheduleDefinition {
        private final ScheduleType scheduleType;
        private final String expression;
    }
}
