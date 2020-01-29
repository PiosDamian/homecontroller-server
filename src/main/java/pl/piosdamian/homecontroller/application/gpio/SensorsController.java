package pl.piosdamian.homecontroller.application.gpio;

import pl.piosdamian.homecontroller.infractructure.rest.dto.response.SensorDTO;
import pl.piosdamian.homecontroller.infractructure.rest.dto.request.SensorUpdateDTO;

import java.io.IOException;
import java.util.List;

public interface SensorsController {
    List<SensorDTO> getSensors();
    void refresh();
    SensorDTO updateSensor(String address, SensorUpdateDTO updateObject) throws IOException;
}
