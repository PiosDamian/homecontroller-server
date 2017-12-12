package pl.piosdamian.homecontroller.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
	@PostMapping("/register")
	public void register(@RequestParam("address") int address,
			@RequestParam("name") String name,
			@RequestParam("role") PinType role) {
		System.out.println(address);
		System.out.println(name);
		System.out.println(role);
		PinModel pin = new PinModel(1, "Pokój 1", PinType.OUTPUT);
		gpioController.addPin(pin);
		PinModel pin2 = new PinModel(2, "Pokój 2", PinType.OUTPUT);
		gpioController.addPin(pin2);
	}

	@CrossOrigin
	@GetMapping("/switch/{id}")
	public void switchState(@PathVariable("id") int id) {
		gpioController.switchVal(id);
	}

	@CrossOrigin
	@GetMapping("/all")
	public List<Map<String, Object>> all() {
		return gpioController.getSwitchers();
	}

}
