package pl.piosdamian.homecontroller.interfaces.rest.dto.request;

import lombok.Data;

@Data
public class SensorUpdateDTO {
    /**
     * name of sensor
     */
    private String name;
    /**
     * sensor address
     */
    private String address;
    /**
     * reading value factor
     */
    private Double factor;
    /**
     * units of value
     */
    private String units;
}
