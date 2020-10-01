package pl.piosdamian.homecontroller.application.model;

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
    private final static Pattern VALUE_PATTERN = Pattern.compile("(?<=t=)\\d+");
    private final static Pattern STATE_PATTER = Pattern.compile("(?<=crd=)\\.+");

    @Setter
    private String name;

    private final W1Device device;

    private double factor = 0.001;

    private double equationConst = 0;

    @Setter
    private String units = "";

    private Double value;

    public SensorDevice(final W1Device device) {
        this.name = device.getName();
        this.device = device;
    }

    public void retrieveValue() {
        final String rawValue;
        try {
            rawValue = device.getValue();
        } catch (IOException e) {
            log.warn("Problem with retrieving value {}, {}", this.name, e.getMessage());
            this.value = null;
            return;
        }

        if (wasAnyError(rawValue)) {
            return;
        }

        final Matcher matcher;
        try {
            matcher = VALUE_PATTERN.matcher(rawValue);
        } catch (NullPointerException e) {
            log.warn("No device under name {}", this.name);
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
        this.retrieveValue();
    }

    public void setEquationConst(double equationConst) {
        this.equationConst = equationConst;
        this.retrieveValue();
    }

    private boolean wasAnyError(String value) {
        final Matcher matcher = STATE_PATTER.matcher(value);
        if (matcher.find()) {
            return matcher.group().toUpperCase().equals("YES");
        }
        return false;
    }

    private void setValue(Double rawValue) {
        this.value = rawValue != null ? rawValue * factor + equationConst : Double.MIN_VALUE;
    }
}
