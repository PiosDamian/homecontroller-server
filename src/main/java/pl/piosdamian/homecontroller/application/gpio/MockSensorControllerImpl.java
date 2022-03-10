package pl.piosdamian.homecontroller.application.gpio;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import pl.piosdamian.homecontroller.interfaces.rest.dto.response.SensorDTO;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Profile("!rasp")
@Slf4j
public class MockSensorControllerImpl implements SensorsController {
    private final Map<String, SensorDTO> sensors = new HashMap<>();

    private MockSensorControllerImpl() {
        sensors.put("1234", new MockSensorDTO(new SensorDTO("1234", null, 12213d, "", 0.001, 0d)));
    }

    @Override
    public List<SensorDTO> getSensors() {
        return new ArrayList<>(this.sensors.values());
    }

    @Override
    @PostConstruct
    public void refresh() {
        this.getValues();
    }

    @Override
    public void getValues() {
        this.sensors.values().forEach(sensorDTO -> sensorDTO.setValue(this.getRandomNumber()));
    }

    @Override
    public SensorDTO updateSensor(final String address, final SensorDTO updateObject) throws IOException {
        sensors.put(address, new MockSensorDTO(updateObject));
        return updateObject;
    }

    private double getRandomNumber() {
        return ((Math.random() * (100000 - 1000)) + 1000);
    }

    private static class MockSensorDTO extends SensorDTO {

        MockSensorDTO(final SensorDTO sensorDTO) {
            super(sensorDTO.getAddress(), sensorDTO.getName(), sensorDTO.getValue(), sensorDTO.getUnits(), sensorDTO.getFactor(), sensorDTO.getEquationConst());
        }

        @Override
        public void setValue(Double value) {
            super.setValue(value * this.getFactor() + this.getEquationConst());
        }
    }
}
