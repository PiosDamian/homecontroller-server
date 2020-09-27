package pl.piosdamian.homecontroller.application.serialization;

import com.fasterxml.jackson.core.type.TypeReference;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.piosdamian.homecontroller.application.configuration.GpioConfiguration;
import pl.piosdamian.homecontroller.application.model.SensorDevice;
import pl.piosdamian.homecontroller.application.model.SwitcherDevice;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static pl.piosdamian.homecontroller.application.utils.Mapper.JSON_MAPPER;

@Service
@Slf4j
@RequiredArgsConstructor
public class PinsConfiguration {
    private final static String PINS_JSON = "pins.json";
    private final static String SENSORS_JSON = "sensors.json";

    private final GpioConfiguration gpioConfiguration;

    public void serializePins(Map<Integer, SwitcherDevice> devices) throws IOException {
        final List<SwitcherSerializationDTO> collect = devices.entrySet()
                .stream()
                .map(entry -> {
                    Integer listenerAddress = null;
                    final GpioPinDigitalInput listener = entry.getValue().getListener();
                    if (listener != null) {
                        listenerAddress = listener.getPin().getAddress();
                    }
                    return new SwitcherSerializationDTO(
                            entry.getKey(),
                            entry.getValue().getName(),
                            listenerAddress);
                })
                .collect(Collectors.toList());

        try (FileOutputStream fos = new FileOutputStream(new File(createConfigFilePath(PINS_JSON)))) {
            fos.write(JSON_MAPPER.writeValueAsBytes(collect));
        }
    }

    public List<SwitcherSerializationDTO> deserializePins() throws IOException {
        final File pinsFile = new File(createConfigFilePath(PINS_JSON));
        if (pinsFile.exists()) {
            try (final FileInputStream fis = new FileInputStream(pinsFile)) {
                return JSON_MAPPER.readValue(fis, new TypeReference<>() {
                });
            } catch (IOException e) {
                log.warn("Can not read pins configuration file");
                throw e;
            }
        } else {
            return Collections.emptyList();
        }
    }

    public void serializeSensors(Map<String, SensorDevice> sensorDeviceMap) throws IOException {
        try (final FileOutputStream fos = new FileOutputStream(createConfigFilePath(SENSORS_JSON))) {
            fos.write(JSON_MAPPER.writeValueAsBytes(sensorDeviceMap));
        }
    }

    public Map<String, SensorDevice> deserializeSensors() throws IOException {
        final File sensorsFile = new File(createConfigFilePath(SENSORS_JSON));
        if (sensorsFile.exists()) {
            try (final FileInputStream fis = new FileInputStream(sensorsFile)) {
                return JSON_MAPPER.readValue(fis, new TypeReference<>() {
                });
            }
        } else {
            return new HashMap<>();
        }
    }

    private String createConfigFilePath(String fileName) {
        final File configRoot = new File(this.gpioConfiguration.getConfigDirectoryPath());
        if (!configRoot.exists()) {
            configRoot.mkdirs();
        }
        return this.gpioConfiguration.getConfigDirectoryPath() + File.separator + fileName;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SwitcherSerializationDTO {
        private Integer address;
        private String name;
        private Integer listenerAddress;
    }
}
