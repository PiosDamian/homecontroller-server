package pl.piosdamian.homecontroller.gpio;

import java.util.ArrayList;
import java.util.List;

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
			switchers.add(gpio.provisionDigitalOutputPin(pin.getPin(),
					pin.getProp().getName(), PinState.LOW));
		}
	}

	public void switchVal(int address) {
		GpioPinDigitalOutput pin = findOutputPin(address);
		pin.toggle();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			pin.setState(PinState.LOW);
		}
		pin.toggle();
	}

	private GpioPinDigitalOutput findOutputPin(int address) {
		System.out.println(Resolver.resolve(address));
		for (GpioPinDigitalOutput pin : switchers) {
			System.out.println(pin);
			if (pin.getPin().equals(Resolver.resolve(address))) {
				return pin;
			}
		}
		return null;
	}

	@SuppressWarnings("unused")
	private GpioPinDigitalInput findInputPin(int address) {
		for (GpioPinDigitalInput pin : watchers) {
			if (pin.getPin() == Resolver.resolve(address)) {
				return pin;
			}
		}
		return null;
	}
}
