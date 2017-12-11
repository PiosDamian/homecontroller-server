package pl.piosdamian.homecontroller.gpio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;

import pl.piosdamian.homecontroller.enums.PinType;
import pl.piosdamian.homecontroller.model.PinModel;

@Controller
public class GpioControl {

	final private GpioController gpio;
	private List<GpioPinDigitalOutput> switchers;
	private List<GpioPinDigitalInput> watchers;

	final static Logger log = Logger.getLogger(GpioControl.class);

	public GpioControl() {
		gpio = GpioFactory.getInstance();
		switchers = new ArrayList<>();
		watchers = new ArrayList<>();
	}

	public void addPin(PinModel pin) {
		if (pin.getProp().getRole() == PinType.INPUT) {
			watchers.add(gpio.provisionDigitalInputPin(pin.getPin(),
					pin.getProp().getName(), PinPullResistance.PULL_DOWN));
		} else if (pin.getProp().getRole() == PinType.OUTPUT) {
			GpioPinDigitalOutput outputPin = gpio.provisionDigitalOutputPin(
					pin.getPin(), pin.getProp().getName(), PinState.LOW);
			outputPin.setShutdownOptions(true, PinState.LOW);
			switchers.add(outputPin);
		}
	}

	public void switchVal(int address) {
		findOutputPin(address).pulse(1000, true);
	}

	private GpioPinDigitalOutput findOutputPin(int address) {
		for (GpioPinDigitalOutput pin : switchers) {
			if (pin.getPin().equals(Resolver.resolve(address))) {
				return pin;
			}
		}
		return null;
	}

	public List<Map<String, Object>> getSwitchers() {
		List<Map<String, Object>> output = new ArrayList<>();

		for (GpioPinDigitalOutput pin : switchers) {
			Map<String, Object> switchersMap = new HashMap<>();
			switchersMap.put("pin", pin.getPin());
			switchersMap.put("name", pin.getName());
			output.add(switchersMap);
		}

		return output;
	}

	public List<GpioPinDigitalInput> getWatchers() {
		return watchers;
	}

	// @SuppressWarnings("unused")
	// private GpioPinDigitalInput findInputPin(int address) {
	// for (GpioPinDigitalInput pin : watchers) {
	// if (pin.getPin() == Resolver.resolve(address)) {
	// return pin;
	// }
	// }
	// return null;
	// }
}
