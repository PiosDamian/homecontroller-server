package pl.piosdamian.homecontroller.interfaces.rest.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.piosdamian.homecontroller.application.model.SensorDevice;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SensorDTO {
    private String address;
    private String name;
    private Double value;
    private String units;
    private double factor;

    public SensorDTO(final String address, final SensorDevice device) {
        this.address = address;
        this.name = device.getName();
        this.value = device.getValue();
        this.units = device.getUnits();
        this.factor = device.getFactor();
    }
}
