package pl.piosdamian.homecontroller.application.scheduling.dto;

import lombok.Data;

import java.util.Map;

@Data
public class TaskDefinition {
    private final String name;
    private final ScheduleType scheduleType;
    private final String expression;
    private final String actionType;
    private final Map<String, String> data;

    public enum ScheduleType {
        CRON, PERIOD
    }
}
