package pl.piosdamian.homecontroller.infractructure.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.piosdamian.homecontroller.application.gpio.SensorsController;
import pl.piosdamian.homecontroller.application.model.SensorUpdateObject;

import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
public class SensorGateway {

    private final SensorsController sensorsController;

    @GetMapping("/sensors")
    public ResponseEntity getSensors() {
        return ResponseEntity.ok(sensorsController.getSensors());
    }

    @PostMapping("/sensor/{address}")
    public ResponseEntity updateSensor(@PathVariable("address") String address, @RequestBody SensorUpdateObject updateObject) {
        try {
            return ResponseEntity.ok(sensorsController.updateSensor(address, updateObject));
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
