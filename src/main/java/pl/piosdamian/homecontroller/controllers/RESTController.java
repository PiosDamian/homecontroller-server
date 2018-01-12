package pl.piosdamian.homecontroller.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pl.piosdamian.homecontroller.enums.PinType;
import pl.piosdamian.homecontroller.gpio.GpioControl;
import pl.piosdamian.homecontroller.model.PinModel;

@RestController
public class RESTController {

	@Autowired
	GpioControl gpioController;

	@CrossOrigin
	@PostMapping("/register/{role}")
	public void register(@RequestParam("address") int address,
			@RequestParam("name") String name,
			@PathVariable("role") PinType role) {

		PinModel pin = new PinModel(address, name, role);
		gpioController.addPin(pin);
	}

	@CrossOrigin
	@GetMapping("/switch/{id}")
	public void switchState(@PathVariable("id") int id) {
		gpioController.switchVal(id);
	}

	@CrossOrigin
	@GetMapping("/all")
	public Map<String, List<Map<String, Object>>> all() {
		Map<String, List<Map<String, Object>>> all = new HashMap<>();
		all.put("switchers", gpioController.getSwitchers());
		all.put("watchers", gpioController.getWatchers());
		return all;
	}

	@CrossOrigin
	@PutMapping("/sensors/{address}")
	public void updateSensor(@RequestParam("name") String name,
			@PathVariable("address") String address) {

	}

	@CrossOrigin
	@GetMapping("/sensors")
	public Map<String, List<Map<String, Object>>> sensors() {
		Map<String, List<Map<String, Object>>> sensors = new HashMap<>();

		return sensors;
	}

	@CrossOrigin
	@DeleteMapping("/delete{address}")
	public void removeDevice(@PathVariable("address") int address) {

	}
}
