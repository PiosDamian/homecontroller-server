package pl.piosdamian.homecontroller.model;

public class Room {
	private PinModel pin;
	boolean turnedOn;

	@Override
	public String toString() {
		return "Room [name=" + pin.getName() + ", turnedOn=" + turnedOn + "]";
	}
}
