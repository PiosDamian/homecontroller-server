package pl.piosdamian.homecontroller.application.gpio;

import pl.piosdamian.homecontroller.interfaces.rest.dto.response.SensorDTO;

import java.io.IOException;
import java.util.List;

public interface SensorsController {
    List<SensorDTO> getSensors();

    void refresh();

    void getValues();

    SensorDTO updateSensor(String address, SensorDTO updateObject) throws IOException;
}
