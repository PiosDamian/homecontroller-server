package pl.piosdamian.homecontroller.gpio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigital;
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
		if (pin.getRole() == PinType.INPUT) {
			watchers.add(createInputPin(pin));
		} else if (pin.getRole() == PinType.OUTPUT) {
			switchers.add(createOutputPin(pin));
		}
	}

	public void switchVal(int address) {
		findOutputPin(address).pulse(1000, true);
	}

	private GpioPinDigitalOutput findOutputPin(int address) {
		for (GpioPinDigitalOutput pin : switchers) {
			if (pin.getPin().getAddress() == address) {
				return pin;
			}
		}
		return null;
	}

	public List<Map<String, Object>> getSwitchers() {
		List<Map<String, Object>> output = new ArrayList<>();

		for (GpioPinDigitalOutput pin : switchers) {
			Map<String, Object> switchersMap = new HashMap<>();
			switchersMap.put("pin", pin.getPin().getAddress());
			switchersMap.put("name", pin.getName());
			output.add(switchersMap);
		}

		return output;
	}

	public List<Map<String, Object>> getWatchers() {
		List<Map<String, Object>> input = new ArrayList<>();

		for (GpioPinDigitalInput pin : watchers) {
			Map<String, Object> watcherMap = new HashMap<>();
			watcherMap.put("pin", pin.getPin().getAddress());
			watcherMap.put("name", pin.getName());
			watcherMap.put("state", pin.getState());
			input.add(watcherMap);
		}
		return input;
	}

	public List<Map<String, Object>> getSensors() {
		List<Map<String, Object>> sensors = new ArrayList<>();

		return sensors;
	}

	public void deleteDevice(int address) {

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

	private GpioPinDigitalInput createInputPin(PinModel pin) {
		GpioPinDigitalInput inputPin = gpio.provisionDigitalInputPin(
				pin.getPin(), pin.getName(), PinPullResistance.PULL_DOWN);

		setShutdown(inputPin);
		return inputPin;
	}

	private GpioPinDigitalOutput createOutputPin(PinModel pin) {
		GpioPinDigitalOutput outputPin = gpio.provisionDigitalOutputPin(
				pin.getPin(), pin.getName(), PinState.LOW);

		setShutdown(outputPin);
		return outputPin;
	}

	private void setShutdown(GpioPinDigital pin) {
		pin.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
	}
}
