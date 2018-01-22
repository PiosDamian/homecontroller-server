package pl.piosdamian.homecontroller.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;

public class ConfigHandler {

	public final static String WATCHERS = "watchers";
	public final static String SWITCHERS = "switchers";

	public static void saveInputPins(List<GpioPinDigitalInput> content) {
		try {
			FileOutputStream fos = new FileOutputStream(WATCHERS);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(content);
			oos.close();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void saveOutputPins(List<GpioPinDigitalOutput> content) {
		try {
			FileOutputStream fos = new FileOutputStream(SWITCHERS);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(content);
			oos.close();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@SuppressWarnings("unchecked")
	public static List<GpioPinDigitalInput> readInputPins() {
		List<GpioPinDigitalInput> output;
		FileInputStream fis;
		ObjectInputStream ois;

		try {
			fis = new FileInputStream(WATCHERS);
			ois = new ObjectInputStream(fis);
			output = (List<GpioPinDigitalInput>) ois.readObject();
			ois.close();
			fis.close();
		} catch (IOException | ClassNotFoundException e) {
			output = new ArrayList<>();
		}

		return output;
	}

	@SuppressWarnings("unchecked")
	public static List<GpioPinDigitalOutput> readOutputPins() {
		List<GpioPinDigitalOutput> output;
		FileInputStream fis;
		ObjectInputStream ois;

		try {
			fis = new FileInputStream(SWITCHERS);
			ois = new ObjectInputStream(fis);
			output = (List<GpioPinDigitalOutput>) ois.readObject();
			ois.close();
			fis.close();
		} catch (IOException | ClassNotFoundException e) {
			output = new ArrayList<>();
		}

		return output;
	}
}
