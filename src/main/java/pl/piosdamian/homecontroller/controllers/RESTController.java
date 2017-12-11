package pl.piosdamian.homecontroller.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.piosdamian.homecontroller.enums.PinType;
import pl.piosdamian.homecontroller.gpio.GpioControl;
import pl.piosdamian.homecontroller.model.PinModel;
import pl.piosdamian.homecontroller.model.RequestModel;

@RestController
public class RESTController {

	@Autowired
	GpioControl gpioController;

	@CrossOrigin
	@RequestMapping("/register")
	public void register() {
		PinModel pin = new PinModel(
				new RequestModel(1, "Pokój 1", PinType.OUTPUT));
		gpioController.addPin(pin);
		PinModel pin2 = new PinModel(
				new RequestModel(2, "Pokój 2", PinType.OUTPUT));
		gpioController.addPin(pin2);
	}

	@CrossOrigin
	@RequestMapping("/switch/{id}")
	public void switchState(@PathVariable("id") int id) {
		gpioController.switchVal(id);
	}

	@CrossOrigin
	@RequestMapping("/all")
	public List<Map<String, Object>> all() {
		return gpioController.getSwitchers();
	}

}
