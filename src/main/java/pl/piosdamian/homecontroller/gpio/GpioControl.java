package pl.piosdamian.homecontroller.gpio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.springframework.stereotype.Controller;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

@Controller
public class GpioControl {

	final private GpioController gpio = GpioFactory.getInstance();
	private List<GpioPinDigitalOutput> switchers = new ArrayList<>();
	private List<GpioPinDigitalInput> watchers = new ArrayList<>();

	private Map<String, Boolean> pinStates;

	public GpioControl() {
		pinStates = new HashMap<>();
		switchers.add(gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01,
				"Controller1", PinState.HIGH));
		watchers.add(gpio.provisionDigitalInputPin(RaspiPin.GPIO_02,
				"Listener1", PinPullResistance.PULL_DOWN));
	}

	public GpioControl(List<Pin> _switchers, List<Pin> _watchers) {
		ListIterator<Pin> switcherIterator = _switchers.listIterator();
		while (switcherIterator.hasNext()) {
			String controller = "Controller"
					+ Integer.toString(switcherIterator.nextIndex());

			switchers.add(gpio.provisionDigitalOutputPin(
					switcherIterator.next(), controller, PinState.HIGH));

			switchers.get(switcherIterator.nextIndex()).setShutdownOptions(true,
					PinState.LOW);
		}

		pinStates = new HashMap<>(_watchers.size());

		ListIterator<Pin> watchersIterator = _watchers.listIterator();
		while (watchersIterator.hasNext()) {

			watchers.add(gpio.provisionDigitalInputPin(watchersIterator.next(),
					PinPullResistance.PULL_DOWN));

			watchers.get(watchersIterator.nextIndex()).setShutdownOptions(true);
		}
	}

	public boolean switchLight(String name) {
		ListIterator<GpioPinDigitalOutput> iterator = switchers.listIterator();
		while (iterator.hasNext()) {
			GpioPinDigitalOutput gpioPin = switchers.get(iterator.nextIndex());
			if (gpioPin.getName().equals(name)) {
				gpioPin.high();
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				gpioPin.low();
				return true;
			}
		}
		return false;
	}

	public Map<String, Boolean> getStates() {
		ListIterator<GpioPinDigitalInput> iterator = watchers.listIterator();
		while (iterator.hasNext()) {
			pinStates.put(watchers.get(iterator.nextIndex()).getName(),
					watchers.get(iterator.nextIndex()).getState()
							.equals(PinState.HIGH));
		}
		return pinStates;
	}

	public ArrayList<String> getSwitchers() {
		ArrayList<String> switchersNames = new ArrayList<String>();
		ListIterator<GpioPinDigitalOutput> iterator = switchers.listIterator();
		while (iterator.hasNext()) {
			switchersNames.add(switchers.get(iterator.nextIndex()).getName());
		}
		return switchersNames;
	}
}
