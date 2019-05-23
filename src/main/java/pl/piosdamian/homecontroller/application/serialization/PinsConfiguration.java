package pl.piosdamian.homecontroller.application.serialization;

import com.fasterxml.jackson.core.type.TypeReference;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.piosdamian.homecontroller.application.model.SensorDevice;
import pl.piosdamian.homecontroller.application.model.SwitcherDevice;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static pl.piosdamian.homecontroller.application.utils.Mapper.JSON_MAPPER;

@Service
@Slf4j
public class PinsConfiguration {
    private final static String PINS_JSON = "pins.json";

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

        try (FileOutputStream fos = new FileOutputStream(new File(PINS_JSON))) {
            fos.write(JSON_MAPPER.writeValueAsBytes(collect));
        }
    }

    public List<SwitcherSerializationDTO> deserializePins() {
        try(final FileInputStream fis = new FileInputStream(new File(PINS_JSON))) {
            return JSON_MAPPER.readValue(fis, new TypeReference<List<SwitcherSerializationDTO>>() {});
        } catch (IOException e) {
            log.warn("Can not read pins configuration file");
        }
        return new ArrayList<>();
    }

    public void serializeSensors(Map<String, SensorDevice> sensorDeviceMap) {}



    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SwitcherSerializationDTO {
        private Integer address;
        private String name;
        private Integer listenerAddress;
    }
}
