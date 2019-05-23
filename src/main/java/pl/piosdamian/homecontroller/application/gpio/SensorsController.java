package pl.piosdamian.homecontroller.application.gpio;

import pl.piosdamian.homecontroller.application.model.SensorDTO;
import pl.piosdamian.homecontroller.application.model.SensorUpdateObject;

import java.util.List;

public interface SensorsController {
    List<SensorDTO> getSensors();
    void refresh();
    SensorDTO updateSensor(String address, SensorUpdateObject updateObject);
}
