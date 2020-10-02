package pl.piosdamian.homecontroller.application.gpio;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import pl.piosdamian.homecontroller.interfaces.rest.dto.response.SwitcherDTO;
import pl.piosdamian.homecontroller.interfaces.rest.dto.response.SwitcherState;

import java.io.IOException;
import java.util.*;

@Service
@Profile("!rasp")
@Slf4j
public class MockGPIOControllerImpl implements GPIOController {

    private final Map<Integer, SwitcherDTO> switchers = new HashMap<>();

    @Override
    public void registerSwitcher(int pinAddress, String name, boolean force) throws IOException {
        log.info("Creating new switcher, address: {}, name: {}", pinAddress, name);
        this.switchers.put(pinAddress, new SwitcherDTO(pinAddress, name, SwitcherState.UNKNOWN, 1));
    }

    @Override
    public void registerSwitcher(int pinAddress, String name, int listenerAddress, boolean force) throws IOException {
        this.registerSwitcher(pinAddress, name, force);
    }

    @Override
    public void deleteSwitcher(int pinAddress) throws IOException {
        log.info("Deleting switcher, address: {}", pinAddress);
        this.switchers.remove(pinAddress);
    }

    @Override
    public void updateListener(int switcherAddress, int listenerAddress) throws IOException {
        log.info("Updating switcher {} with listener {}", switcherAddress, listenerAddress);
    }

    @Override
    public void updateName(int switcherAddress, String name) throws IOException {
        if(this.switchers.containsKey(switcherAddress)) {
            log.info("Updating name of switcher {}, new nam: {}", switcherAddress, name);
        }
    }

    @Override
    public void blink(int address) {
        log.info("Blinked switcher {}", address);
    }

    @Override
    public Collection<SwitcherDTO> getSwitchers() {
        return new ArrayList<>(this.switchers.values());
    }

    @Override
    public Set<Integer> getReservedPins() {
        return this.switchers.keySet();
    }
}
