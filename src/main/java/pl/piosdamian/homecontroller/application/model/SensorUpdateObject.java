package pl.piosdamian.homecontroller.application.model;

import lombok.Data;

@Data
public class SensorUpdateObject {
    private String name;
    private Double multiplier;
    private String units;
}
