package pl.piosdamian.homecontroller.application.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pi4j.io.w1.W1Device;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@Data
@Slf4j
public class SensorDevice {
    @JsonIgnore
    private final static Pattern VALUE_PATTERN = Pattern.compile("(?<=t=)\\d+");

    @Setter
    private String name;

    @JsonIgnore
    @Setter
    private W1Device device;


    private double factor = 0.001;

    @Setter
    private String units = "";

    @JsonIgnore
    private Double value;

    @JsonIgnore
    public void retrieveValue() {
        final Matcher matcher;
        try {
            matcher = VALUE_PATTERN.matcher(device.getValue());
        } catch (NullPointerException e) {
            log.warn("No device under name {}", this.name);
            this.value = null;
            return;
        } catch (IOException e) {
            log.warn("Problem with retrieving value {}, {}", this.name, e.getMessage());
            this.value = null;
            return;
        }

        Double value = null;
        if (matcher.find()) {
            value = Double.parseDouble(matcher.group());
        }
        this.setValue(value);
    }


    public void setFactor(double factor) {
        this.factor = factor;
        this.setValue(this.value);
    }

    private void setValue(Double rawValue) {
        this.value = rawValue != null ? rawValue * factor : Double.MIN_VALUE;
    }
}
