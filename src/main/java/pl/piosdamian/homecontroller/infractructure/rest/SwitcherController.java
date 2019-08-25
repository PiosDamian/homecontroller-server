package pl.piosdamian.homecontroller.infractructure.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.piosdamian.homecontroller.application.gpio.GPIOController;
import pl.piosdamian.homecontroller.infractructure.rest.dto.SwitcherRequestBody;
import pl.piosdamian.homecontroller.infractructure.rest.dto.UpdateSwitcherRequestBody;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class SwitcherController {

    private final GPIOController gpioController;

    @GetMapping("/switch/{address}")
    public ResponseEntity switchPin(@PathVariable("address") int address) {
        this.gpioController.blink(address);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/switchers")
    public ResponseEntity getSwitchers() {
        return ResponseEntity.ok(gpioController.getDevices());
    }

    @PostMapping("/switcher")
    public ResponseEntity registerSwitcher(@RequestBody SwitcherRequestBody switcherRequestBody) throws IOException {
        Integer listenerAddress = switcherRequestBody.getListenerAddress();
        if(listenerAddress == null) {
            this.gpioController.registerSwitcher(switcherRequestBody.getPinAddress(), switcherRequestBody.getName(), switcherRequestBody.isForce());
        } else {
            this.gpioController.registerSwitcher(switcherRequestBody.getPinAddress(), switcherRequestBody.getName(), listenerAddress, switcherRequestBody.isForce());
        }

        return ResponseEntity.ok().build();
    }

    @PutMapping("/switcher/{address}")
    public ResponseEntity addListener(@PathVariable("address") int switcherAddress, @RequestBody UpdateSwitcherRequestBody body) throws IOException {
        if (body.getName() != null) {
            this.gpioController.updateName(switcherAddress, body.getName());
        }
        if(body.getListenerAddress() != null) {
            this.gpioController.updateListener(switcherAddress, body.getListenerAddress());
        }

        if(body.getName() == null && body.getListenerAddress() == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/switcher/{address}")
    public ResponseEntity deleteSwitcher(@PathVariable("address") int address) throws IOException {
        this.gpioController.deleteSwitcher(address);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(IOException.class)
    private ResponseEntity handleIOException() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Configuration could not be stored");
    }
}
