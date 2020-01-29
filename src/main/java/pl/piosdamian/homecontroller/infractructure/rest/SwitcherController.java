package pl.piosdamian.homecontroller.infractructure.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.piosdamian.homecontroller.application.gpio.GPIOController;
import pl.piosdamian.homecontroller.infractructure.rest.dto.request.RequestSwitcherDTO;
import pl.piosdamian.homecontroller.infractructure.rest.dto.response.SwitcherDTO;

import java.io.IOException;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
public class SwitcherController {

    private final GPIOController gpioController;

    @GetMapping("/switch/{address}")
    public ResponseEntity<Void> switchPin(@PathVariable("address") int address) {
        this.gpioController.blink(address);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/switchers")
    public ResponseEntity<Collection<SwitcherDTO>> getSwitchers() {
        return ResponseEntity.ok(gpioController.getSwitchers());
    }

    @PostMapping("/switcher/{address}")
    public ResponseEntity<Void> registerSwitcher(@PathVariable("address") int switcherAddress, @RequestBody RequestSwitcherDTO switcherExtendedDTO) throws IOException {
        Integer listenerAddress = switcherExtendedDTO.getListenerAddress();
        if (listenerAddress == null) {
            this.gpioController.registerSwitcher(switcherAddress, switcherExtendedDTO.getName(), switcherExtendedDTO.isForce());
        } else {
            this.gpioController.registerSwitcher(switcherAddress, switcherExtendedDTO.getName(), listenerAddress, switcherExtendedDTO.isForce());
        }

        return ResponseEntity.ok().build();
    }

    @PutMapping("/switcher/{address}")
    public ResponseEntity<Void> addListener(@PathVariable("address") int switcherAddress, @RequestBody RequestSwitcherDTO body) throws IOException {
        if (body.getName() != null) {
            this.gpioController.updateName(switcherAddress, body.getName());
        }
        if (body.getListenerAddress() != null) {
            this.gpioController.updateListener(switcherAddress, body.getListenerAddress());
        }

        if (body.getName() == null && body.getListenerAddress() == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/switcher/{address}")
    public ResponseEntity<Void> deleteSwitcher(@PathVariable("address") int address) throws IOException {
        this.gpioController.deleteSwitcher(address);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(IOException.class)
    private ResponseEntity<String> handleIOException() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Configuration could not be stored");
    }
}
