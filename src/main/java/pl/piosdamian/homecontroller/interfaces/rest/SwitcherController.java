package pl.piosdamian.homecontroller.interfaces.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.piosdamian.homecontroller.application.gpio.GPIOController;
import pl.piosdamian.homecontroller.interfaces.rest.dto.request.RequestSwitcherDTO;
import pl.piosdamian.homecontroller.interfaces.rest.dto.response.SwitcherDTO;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@Slf4j
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

    @GetMapping("/reservedPins")
    public ResponseEntity<Set<Integer>> getReservedPins() {
        return ResponseEntity.ok(this.gpioController.getReservedPins());
    }

    @PostMapping("/switcher/add")
    public ResponseEntity<Void> registerSwitcher(@RequestBody final RequestSwitcherDTO switcherDTO) throws IOException {
        Integer listenerAddress = switcherDTO.getListenerAddress();
        if (listenerAddress == null) {
            this.gpioController.registerSwitcher(switcherDTO.getAddress(), switcherDTO.getName(), switcherDTO.isForce());
        } else {
            this.gpioController.registerSwitcher(switcherDTO.getAddress(), switcherDTO.getName(), listenerAddress, switcherDTO.isForce());
        }

        return ResponseEntity.ok().build();
    }

    @PutMapping("/switcher/update")
    public ResponseEntity<Void> addListener(@RequestBody final RequestSwitcherDTO switcherDTO) throws IOException {
        if (switcherDTO.getName() != null) {
            this.gpioController.updateName(switcherDTO.getAddress(), switcherDTO.getName());
        }
        if (switcherDTO.getListenerAddress() != null) {
            this.gpioController.updateListener(switcherDTO.getAddress(), switcherDTO.getListenerAddress());
        }

        if (switcherDTO.getName() == null && switcherDTO.getListenerAddress() == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/switcher/{address}")
    public ResponseEntity<Void> deleteSwitcher(@PathVariable("address") final int address) throws IOException {
        this.gpioController.deleteSwitcher(address);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(IOException.class)
    private ResponseEntity<String> handleIOException(IOException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Configuration could not be stored");
    }
}
