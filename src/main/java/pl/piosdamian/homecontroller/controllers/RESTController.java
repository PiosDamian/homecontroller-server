package pl.piosdamian.homecontroller.controllers;

import java.util.ArrayList;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import pl.piosdamian.homecontroller.gpio.GpioControl;

@RestController
public class RESTController {

	@Autowired
	GpioControl gpioController;

	@RequestMapping("/controllers")
	public ArrayList<String> control() {
		return gpioController.getSwitchers();
	}

	@RequestMapping("/states")
	public String states() {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(gpioController.getStates());
		} catch (JsonProcessingException e) {
			return "Internal error";
		}
	}

	@RequestMapping("/control/{name}")
	public String switchLight(@PathParam("name") String name) {
		return gpioController.switchLight(name) ? "Done"
				: "redirect:/controllers";
	}

	@RequestMapping("/register")
	public void register() {

	}
}
