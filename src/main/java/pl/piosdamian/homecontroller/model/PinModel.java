package pl.piosdamian.homecontroller.model;

import pl.piosdamian.homecontroller.enums.PinType;
import pl.piosdamian.homecontroller.gpio.Resolver;

public class PinModel {
	private com.pi4j.io.gpio.Pin pin;
	private int address;
	private String name;
	private PinType role;

	public PinModel(int address, String name, PinType role) {
		this.pin = Resolver.resolve(address);
		this.address = address;
		this.name = name;
		this.role = role;
	}

	public com.pi4j.io.gpio.Pin getPin() {
		return pin;
	}

	public int getAddress() {
		return address;
	}

	public String getName() {
		return name;
	}

	public PinType getRole() {
		return role;
	}

	@Override
	public String toString() {
		return "PinModel [pin=" + pin + ", address=" + address + ", name="
				+ name + ", role=" + role + "]";
	}
}
