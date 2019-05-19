package pl.piosdamian.homecontroller.application.model;

import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import lombok.Data;

@Data
public class SwitcherDevice {
    private GpioPinDigitalOutput blinker;
    private String name;
    private GpioPinDigitalInput listener;
    private SwitcherState state = SwitcherState.UNKNOWN;

    public void setBlinker(GpioPinDigitalOutput blinker) {
        blinker.setShutdownOptions(true, PinState.LOW);
        this.blinker = blinker;
    }

    public void setListener(GpioPinDigitalInput listener) {
        listener.setShutdownOptions(true);
        this.listener = listener;
    }
}
