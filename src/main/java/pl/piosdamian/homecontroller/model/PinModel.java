package pl.piosdamian.homecontroller.model;

import pl.piosdamian.homecontroller.gpio.Resolver;

public class PinModel {
	private com.pi4j.io.gpio.Pin pin;
	private RequestModel prop;

	public PinModel(RequestModel prop) {
		this.pin = new Resolver().resolve(prop.getAddress());
		this.prop = prop;
	}

	public com.pi4j.io.gpio.Pin getPin() {
		return pin;
	}

	public RequestModel getProp() {
		return prop;
	}

	@Override
	public String toString() {
		return "PinModel [pin=" + pin + ", prop=" + prop + "]";
	}

}
