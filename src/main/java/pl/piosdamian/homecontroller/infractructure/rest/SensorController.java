package pl.piosdamian.homecontroller.infractructure.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.piosdamian.homecontroller.application.gpio.SensorsController;
import pl.piosdamian.homecontroller.infractructure.rest.dto.request.SensorUpdateDTO;

import java.io.IOException;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
public class SensorController {

    private final SensorsController sensorsController;

    @GetMapping("/sensors")
    public ResponseEntity getSensors() {
        return ResponseEntity.ok(sensorsController.getSensors());
    }

    @PostMapping("/sensor/{address}")
    public ResponseEntity updateSensor(@PathVariable String address, @RequestBody SensorUpdateDTO updateObject) {
        try {
            return ResponseEntity.ok(sensorsController.updateSensor(address, updateObject));
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().build();
        } catch (IOException ioe) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not store configuration");
        }
    }
}
