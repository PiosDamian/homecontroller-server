package pl.piosdamian.homecontroller.application.gpio;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import pl.piosdamian.homecontroller.application.model.SwitcherDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Primary
@Slf4j
@Profile("!rasp")
public class MockGPIOController implements GPIOController {
    private Map<Integer, Device> devices = new HashMap<>();
    @Getter
    private List<Integer> calls = new ArrayList<>();

    @Override
    public void registerSwitcher(int pinAddress, String name) {
        devices.put(pinAddress, new Device(name, null));
    }

    @Override
    public void registerSwitcher(int pinAddress, String name, int listenerAddress) {
        devices.put(pinAddress, new Device(name, listenerAddress));
    }

    @Override
    public void deleteSwitcher(int pinAddress) {
        this.devices.remove(pinAddress);
    }

    @Override
    public void updateListener(int switcherAddress, int listenerAddress) {
        devices.get(switcherAddress).setListenerAddress(listenerAddress);
    }

    @Override
    public void updateName(int switcherAddress, String name) {
        devices.get(switcherAddress).setName(name);
    }

    @Override
    public void blink(int address) {
        new Thread(() -> {
            calls.add(address);
            log.info("Blink request for {}: {}", address, devices.get(address));
        }).run();
    }

    @Override
    public List<SwitcherDTO> getDevices() {
        return devices.entrySet().stream().map(entry -> new SwitcherDTO(entry.getKey(), entry.getValue().getName(), "OFF")).collect(Collectors.toList());
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class Device {
        private String name;
        private Integer listenerAddress;
    }
}