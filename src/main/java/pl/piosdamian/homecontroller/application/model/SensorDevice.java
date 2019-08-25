package pl.piosdamian.homecontroller.application.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pi4j.io.w1.W1Device;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
@Slf4j
public class SensorDevice {
    @JsonIgnore
    private final static Pattern VALUE_PATTERN = Pattern.compile("\\d+");
    private String name;
    @JsonIgnore
    private W1Device device;
    private double factory = 1;
    private String units = "";

    @JsonIgnore
    public Double getDeviceValue() throws IOException {
        final Matcher matcher;
        try {
            matcher = VALUE_PATTERN.matcher(device.getValue());
        } catch (NullPointerException e) {
            log.warn("No device under name {}", this.name);
            return null;
        }

        Double value = null;
        while (matcher.find()) {
            value = Double.parseDouble(matcher.group());
        }
        return value != null? value * factory: value;
    }
}
