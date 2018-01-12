package pl.piosdamian.homecontroller.utils;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;

import pl.piosdamian.homecontroller.gpio.GpioControl;

public class JSON {

	@Autowired
	GpioControl gpio;

	@SuppressWarnings("unchecked")
	public void saveSettings() {
		JSONObject obj = new JSONObject();

		obj.put("app", "homeController");

		JSONArray watchers = new JSONArray();
		JSONArray switchers = new JSONArray();

		watchers.addAll(gpio.getWatchers());
		switchers.addAll(gpio.getSwitchers());

		obj.put("watchers", watchers);
		obj.put("switchers", switchers);

		try (FileWriter file = new FileWriter("settings.json")) {
			file.write(obj.toJSONString());
			file.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void readSettings() {
		JSONParser parser = new JSONParser();

		try {
			JSONObject obj = (JSONObject) parser
					.parse(new FileReader("settings.json"));

			JSONArray watchers = (JSONArray) obj.get("watchers");
			JSONArray switchers = (JSONArray) obj.get("switchers");

		} catch (IOException | ParseException e) {
			e.printStackTrace();
		} finally {

		}
	}
}
