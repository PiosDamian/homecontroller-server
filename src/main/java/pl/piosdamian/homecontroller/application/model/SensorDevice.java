package pl.piosdamian.homecontroller.application.model;

import com.pi4j.io.w1.W1Device;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
@Slf4j
public class SensorDevice {
    private final static Pattern VALUE_PATTERN = Pattern.compile("\\d+");
    private String name;
    private W1Device device;
    private double multiplier = 1;
    private String units = "";

    public String getDeviceValue() throws IOException {
        final Matcher matcher;
        try {
        matcher = VALUE_PATTERN.matcher(device.getValue());
        } catch (NullPointerException e) {
            log.warn("No device under name {}", this.name);
            return "";
        }

        Double value = null;
        while (matcher.find()) {
            value = Double.parseDouble(matcher.group());
        }

        String tmpUnits;
        if(StringUtils.isNotBlank(this.units)) {
            tmpUnits = " " + this.units;
        } else {
            tmpUnits = "";
        }

        if(value != null) {
            return value * this.multiplier + units;
        }
        return "";
    }
}
