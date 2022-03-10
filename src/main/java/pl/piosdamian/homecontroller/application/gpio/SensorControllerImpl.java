package pl.piosdamian.homecontroller.application.gpio;

import com.pi4j.io.w1.W1Master;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import pl.piosdamian.homecontroller.application.model.SensorDevice;
import pl.piosdamian.homecontroller.application.serialization.PinsConfiguration;
import pl.piosdamian.homecontroller.interfaces.rest.dto.response.SensorDTO;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Profile("rasp")
public class SensorControllerImpl implements SensorsController {

    private final Map<String, SensorDevice> sensors = new HashMap<>();

    private final PinsConfiguration pinsConfiguration;

    public SensorControllerImpl(PinsConfiguration pinsConfiguration) {
        this.pinsConfiguration = pinsConfiguration;
    }

    @Override
    @PostConstruct
    public void refresh() {
        sensors.clear();
        try {
            final W1Master w1Master = new W1Master();
            w1Master.getDevices().forEach(w1Device -> {
                final SensorDevice sensorDevice = new SensorDevice(w1Device);
                sensors.put(w1Device.getName(), sensorDevice);
            });
            pinsConfiguration.deserializeSensors().forEach((name, storedSensor) -> {
                if (this.sensors.containsKey(name)) {
                    final SensorDevice sensorDevice = this.sensors.get(name);
                    sensorDevice.setName(storedSensor.getName());
                    sensorDevice.setFactor(storedSensor.getFactor());
                    sensorDevice.setUnits(storedSensor.getUnits());
                    sensorDevice.setEquationConst(storedSensor.getEquationConst());
                }
            });
            this.getValues();
        } catch (IOException e) {
            log.warn("Problem with retrieving 1-wire devices, {}", e.getMessage());
        }
    }

    @Override
    public void getValues() {
        sensors.values().forEach(SensorDevice::retrieveValue);
    }

    @Override
    public List<SensorDTO> getSensors() {
        return this.sensors.entrySet()
                .stream()
                .map(sensorDeviceEntry -> new SensorDTO(
                        sensorDeviceEntry.getKey(),
                        sensorDeviceEntry.getValue()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public SensorDTO updateSensor(String address, SensorDTO updateObject) throws IOException {
        final Optional<SensorDevice> optionalSensorDevice = Optional.ofNullable(this.sensors.get(address));
        if (optionalSensorDevice.isPresent()) {
            final SensorDevice sensorDevice = optionalSensorDevice.get();

            if (StringUtils.isNotBlank(updateObject.getName())) {
                sensorDevice.setName(updateObject.getName());
            }

            Optional.ofNullable(updateObject.getFactor()).ifPresent(sensorDevice::setFactor);
            Optional.ofNullable(updateObject.getEquationConst()).ifPresent(sensorDevice::setEquationConst);

            if (StringUtils.isNotBlank(updateObject.getUnits())) {
                sensorDevice.setUnits(updateObject.getUnits());
            }

            pinsConfiguration.serializeSensors(this.sensors);
            return new SensorDTO(address, sensorDevice);
        } else {
            throw new NoSuchElementException("Address not found");
        }
    }
}
