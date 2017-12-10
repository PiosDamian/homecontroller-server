package pl.piosdamian.homecontroller.controllers;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;

import pl.piosdamian.homecontroller.enums.PinType;
import pl.piosdamian.homecontroller.gpio.GpioControl;
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

	@RequestMapping("/switch/{id}")
	public Pin switchState(@PathParam("id") Integer id) {
		// System.out.println(id);
		// gpioController.switchVal(id);
		return RaspiPin.GPIO_01;
	}

}
