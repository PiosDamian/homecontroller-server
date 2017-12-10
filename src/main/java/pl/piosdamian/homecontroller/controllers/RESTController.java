package pl.piosdamian.homecontroller.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pi4j.io.gpio.Pin;

import pl.piosdamian.homecontroller.enums.PinType;
import pl.piosdamian.homecontroller.gpio.GpioControl;
import pl.piosdamian.homecontroller.gpio.Resolver;
import pl.piosdamian.homecontroller.model.PinModel;
import pl.piosdamian.homecontroller.model.RequestModel;

@RestController
public class RESTController {

	@Autowired
	GpioControl gpioController;

	@RequestMapping("/register")
	public void register() {
		PinModel pin = new PinModel(
				new RequestModel(1, "Pokój 1", PinType.OUTPUT));
		gpioController.addPin(pin);
	}

	@RequestMapping("/all")
	public Pin all() {
		return new Resolver().resolve(1);
	}

	@RequestMapping("/switch/{id}")
	public int switchState(@PathVariable("id") Integer id) {
		// gpioController.switchVal(id.intValue());
		return id.intValue();
	}

}
