package pl.piosdamian.homecontroller.interfaces.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.piosdamian.homecontroller.application.gpio.SensorsController;
import pl.piosdamian.homecontroller.interfaces.rest.dto.request.SensorUpdateDTO;
import pl.piosdamian.homecontroller.interfaces.rest.dto.response.SensorDTO;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
public class SensorRestController {

    private final SensorsController sensorsController;

    @GetMapping("/sensors")
    public ResponseEntity<List<SensorDTO>> getSensors() {
        return ResponseEntity.ok(sensorsController.getSensors());
    }

    @PutMapping("/sensor/{address}")
    public ResponseEntity<Object> updateSensor(@RequestBody final SensorUpdateDTO sensorUpdateDTO) {
        try {
            return ResponseEntity.ok(sensorsController.updateSensor(sensorUpdateDTO.getAddress(), sensorUpdateDTO));
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().build();
        } catch (IOException ioe) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not store configuration");
        }
    }
}
