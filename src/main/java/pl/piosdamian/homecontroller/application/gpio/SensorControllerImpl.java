package pl.piosdamian.homecontroller.application.gpio;

import com.pi4j.io.w1.W1Master;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import pl.piosdamian.homecontroller.application.model.SensorDTO;
import pl.piosdamian.homecontroller.application.model.SensorDevice;
import pl.piosdamian.homecontroller.application.model.SensorUpdateObject;
import pl.piosdamian.homecontroller.application.serialization.PinsConfiguration;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SensorControllerImpl implements SensorsController {

    private Map<String, SensorDevice> sensors = new HashMap<>();

    private final PinsConfiguration pinsConfiguration;

    public SensorControllerImpl(PinsConfiguration pinsConfiguration) {
        this.pinsConfiguration = pinsConfiguration;
        this.refresh();
    }

    @Override
    public void refresh() {
        sensors.clear();
        try {
            final W1Master w1Master = new W1Master();
            w1Master.getDevices().forEach(w1Device -> {
                final SensorDevice sensorDevice = new SensorDevice();
                sensorDevice.setName(w1Device.getName());
                sensorDevice.setDevice(w1Device);
                sensors.put(w1Device.getName(), sensorDevice);
            });
            pinsConfiguration.deserializeSensors().forEach((name, storedSensor) -> {
                if(this.sensors.containsKey(name)) {
                    final SensorDevice sensorDevice = this.sensors.get(name);
                    sensorDevice.setName(storedSensor.getName());
                    sensorDevice.setFactory(storedSensor.getFactory());
                    sensorDevice.setUnits(storedSensor.getUnits());
                }
            });
        } catch (Throwable t) {
            log.warn("Problem with retrieving 1-wire devices");
        }
    }

    @Override
    public List<SensorDTO> getSensors() {
        return this.sensors.entrySet()
                .stream()
                .map(sensorDeviceEntry -> {
                    Double value = null;
                    try {
                        value = sensorDeviceEntry.getValue().getDeviceValue();
                    } catch (IOException e) {
                        log.warn("Can not read value for {}", sensorDeviceEntry.getKey());
                    }
                    return new SensorDTO(
                            sensorDeviceEntry.getKey(),
                            sensorDeviceEntry.getValue().getName(),
                            value,
                            sensorDeviceEntry.getValue().getUnits()
                    );
                })
                .collect(Collectors.toList());
    }

    @Override
    public SensorDTO updateSensor(String address, SensorUpdateObject updateObject) throws IOException {
        final Optional<SensorDevice> optionalSensorDevice = Optional.ofNullable(this.sensors.get(address));
        if (optionalSensorDevice.isPresent()) {
            final SensorDevice sensorDevice = optionalSensorDevice.get();

            if (StringUtils.isNotBlank(updateObject.getName())) {
                sensorDevice.setName(updateObject.getName());
            }

            Optional.ofNullable(updateObject.getMultiplier()).ifPresent(sensorDevice::setFactory);

            if (Objects.nonNull(updateObject.getUnits())) {
                sensorDevice.setUnits(updateObject.getUnits());
            }

            Double deviceValue;
            try {
                deviceValue = sensorDevice.getDeviceValue();
            } catch (IOException e) {
                deviceValue = null;
            }
            pinsConfiguration.serializeSensors(this.sensors);
            return new SensorDTO(address, sensorDevice.getName(), deviceValue, sensorDevice.getUnits());
        } else {
            throw new NoSuchElementException("Address not found");
        }
    }
}
