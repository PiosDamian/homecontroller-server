package pl.piosdamian.homecontroller.infractructure.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.piosdamian.homecontroller.application.gpio.GPIOController;

@RestController
@RequiredArgsConstructor
public class Gateway {

    private final GPIOController gpioController;

    @GetMapping("/switch/{address}")
    public ResponseEntity switchPin(@PathVariable("address") int address) {
        this.gpioController.blink(address);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/switcher")
    public ResponseEntity registerSwitcher(@RequestBody SwitcherRequestBody switcherRequestBody) {
        Integer listenerAddress = switcherRequestBody.getListenerAddress();
        if(listenerAddress == null) {
            this.gpioController.registerSwitcher(switcherRequestBody.getPinAddress(), switcherRequestBody.getName());
        } else {
            this.gpioController.registerSwitcher(switcherRequestBody.getPinAddress(), switcherRequestBody.getName(), listenerAddress);
        }

        return ResponseEntity.ok().build();
    }

    @PutMapping("/switcher/{address}")
    public ResponseEntity addListener(@PathVariable("address") int switcherAddress, @RequestBody UpdateSwitcherRequestBody body) {
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
    public ResponseEntity deleteSwitcher(@PathVariable("address") int address) {
        this.gpioController.deleteSwitcher(address);
        return ResponseEntity.ok().build();
    }
}
