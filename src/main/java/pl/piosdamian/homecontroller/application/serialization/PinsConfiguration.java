package pl.piosdamian.homecontroller.application.serialization;

import com.pi4j.io.gpio.GpioPinDigitalInput;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import pl.piosdamian.homecontroller.application.model.SensorDevice;
import pl.piosdamian.homecontroller.application.model.SwitcherDTO;
import pl.piosdamian.homecontroller.application.model.SwitcherDevice;
import pl.piosdamian.homecontroller.application.utils.Mapper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static pl.piosdamian.homecontroller.application.utils.Mapper.JSON_MAPPER;

@Service
public class PinsConfiguration {
    private final static String FILENAME = "pins.json";

    public void serializePins(Map<Integer, SwitcherDevice> devices) throws IOException {
        final List<SwitcherDTO> collect = devices.entrySet()
                .stream()
                .map(entry -> {
                    Integer listenerAddress = null;
                    final GpioPinDigitalInput listener = entry.getValue().getListener();
                    if (listener != null) {
                        listenerAddress = listener.getPin().getAddress();
                    }
                    return new SwitcherDTO(
                            entry.getKey(),
                            entry.getValue().getName(),
                            listenerAddress);
                })
                .collect(Collectors.toList());

        try (FileOutputStream fos = new FileOutputStream(new File(FILENAME))) {
            fos.write(JSON_MAPPER.writeValueAsBytes(collect));
        }
    }

    public void serializeSensors(Map<String, SensorDevice> sensorDeviceMap) {}
    


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class SwitcherDTO {
        private Integer address;
        private String name;
        private Integer listenerAddress;
    }
}
