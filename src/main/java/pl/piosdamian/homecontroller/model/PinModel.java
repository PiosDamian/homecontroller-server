package pl.piosdamian.homecontroller.model;

import pl.piosdamian.homecontroller.enums.PinType;

public class PinModel {
	private com.pi4j.io.gpio.Pin pin;
	private int address;
	private String name;
	private PinType role;

	public PinModel(com.pi4j.io.gpio.Pin pin, int address, String name,
			PinType role) {
		this.pin = pin;
		this.address = address;
		this.name = name;
		this.role = role;
	}

	public com.pi4j.io.gpio.Pin getPin() {
		return pin;
	}

	public String getName() {
		return name;
	}

	public PinType getRole() {
		return role;
	}

	@Override
	public String toString() {
		return "Pin [address=" + address + ", name=" + name + ", role=" + role
				+ "]";
	}
}
