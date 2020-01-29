package pl.piosdamian.homecontroller.infractructure.rest.dto.request;

import lombok.Data;

@Data
public class SensorUpdateDTO {
    /**
     * name of sensor
     */
    private String name;
    /**
     * reading value factor
     */
    private Double factor;
    /**
     * units of value
     */
    private String units;
}
