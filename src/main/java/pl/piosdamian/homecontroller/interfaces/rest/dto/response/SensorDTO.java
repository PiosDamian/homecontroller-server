package pl.piosdamian.homecontroller.interfaces.rest.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SensorDTO {
    private String address;
    private String name;
    private Double value;
    private String units;
}
