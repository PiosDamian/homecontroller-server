package pl.piosdamian.homecontroller.model;

import pl.piosdamian.homecontroller.enums.PinType;

public class RequestModel {
	private int address;
	private String name;
	private PinType role;

	public RequestModel(int address, String name, PinType role) {
		this.address = address;
		this.name = name;
		this.role = role;
	}

	public int getAddress() {
		return address;
	}

	public void setAddress(int address) {
		this.address = address;
	}

	public PinType getRole() {
		return role;
	}

	public void setRole(PinType role) {
		this.role = role;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
