package pl.piosdamian.homecontroller.application.scheduling.dto;

import lombok.Data;

import java.util.Map;

@Data
public class TaskDefinition {
    private final String name;
    private final ControlType controlType;
    private final String expression;
    private final Type actionType;
    private final Map<String, String> data;

    public enum Type {
        SWITCH, READ_VALUE
    }

    public enum ControlType {
        CRON, PERIOD
    }
}
