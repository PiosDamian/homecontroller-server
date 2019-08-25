package pl.piosdamian.homecontroller.application.gpio;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import pl.piosdamian.homecontroller.application.configuration.GpioConfiguration;
import pl.piosdamian.homecontroller.application.model.SwitcherDTO;
import pl.piosdamian.homecontroller.application.model.SwitcherDevice;
import pl.piosdamian.homecontroller.application.model.SwitcherState;
import pl.piosdamian.homecontroller.application.serialization.PinsConfiguration;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@Profile("rasp")
public class GPIOControllerImpl implements GPIOController {
    private final GpioController gpio;
    private final GpioConfiguration gpioConfiguration;
    private final PinsConfiguration pinsConfiguration;

    private Map<Integer, SwitcherDevice> switcherDevices = new HashMap<>();

    public GPIOControllerImpl(GpioConfiguration gpioConfiguration, PinsConfiguration pinsConfiguration) throws IOException {
        this.gpioConfiguration = gpioConfiguration;
        this.pinsConfiguration = pinsConfiguration;
        this.gpio = GpioFactory.getInstance();
        pinsConfiguration.deserializePins().forEach(switcher -> {
            if (switcher.getListenerAddress() == null) {
                createSwitcherDevice(switcher.getAddress(), switcher.getName(), true);
            } else {
                createSwitcherWithListener(switcher.getAddress(), switcher.getName(), switcher.getListenerAddress(), true);
            }
        });
    }

    @Override
    public void registerSwitcher(int pinAddress, String name, boolean force) throws IOException {
        createSwitcherDevice(pinAddress, name, force);
        pinsConfiguration.serializePins(this.switcherDevices);
    }

    @Override
    public void registerSwitcher(int pinAddress, String name, int listenerAddress, boolean force) throws IOException {
        createSwitcherWithListener(pinAddress, name, listenerAddress, force);
        pinsConfiguration.serializePins(this.switcherDevices);
    }

    private void createSwitcherWithListener(int pinAddress, String name, int listenerAddress, boolean force) {
        SwitcherDevice switcherDevice = createSwitcherDevice(pinAddress, name, force);
        addListener(createInputPin(listenerAddress), switcherDevice);
    }

    @Override
    public void deleteSwitcher(int pinAddress) throws IOException {
        SwitcherDevice switcherDevice = this.switcherDevices.remove(pinAddress);
        Optional.ofNullable(switcherDevice.getListener()).ifPresent(GpioPin::removeAllListeners);
        pinsConfiguration.serializePins(this.switcherDevices);
    }

    @Override
    public void updateListener(int switcherAddress, int listenerAddress) throws IOException {
        Optional.of(this.switcherDevices.get(switcherAddress)).ifPresent(switcher -> {
            Optional.ofNullable(switcher.getListener()).ifPresent(GpioPin::removeAllListeners);
            addListener(createInputPin(listenerAddress), switcher);
        });
        pinsConfiguration.serializePins(this.switcherDevices);
    }

    @Override
    public void updateName(int switcherAddress, String name) throws IOException {
        this.switcherDevices.get(switcherAddress).setName(name);
        pinsConfiguration.serializePins(this.switcherDevices);
    }

    public void blink(int address) {
        new Thread(() ->
                switcherDevices
                        .get(address)
                        .getBlinker()
                        .pulse(gpioConfiguration.getOutput().getBlinkDuration())).start();
    }

    @Override
    public List<SwitcherDTO> getDevices() {
        return this.switcherDevices
                .entrySet()
                .stream()
                .map(entry -> new SwitcherDTO(entry.getKey(), entry.getValue().getName(), entry.getValue().getState().name()))
                .collect(Collectors.toList());
    }

    private SwitcherDevice createSwitcherDevice(int pinAddress, String name, boolean force) {
        if (switcherDevices.containsKey(pinAddress) && !force) {
            throw new GpioControllerException("Pin already in use");
        }
        if (switcherDevices.containsKey(pinAddress)) {
            Optional.ofNullable(switcherDevices.get(pinAddress).getListener()).ifPresent(GpioPin::removeAllListeners);
        }
        SwitcherDevice switcherDevice = new SwitcherDevice();
        switcherDevice.setBlinker(createOutputPin(pinAddress, name));
        switcherDevice.setName(name);
        return switcherDevices.put(pinAddress, switcherDevice);
    }

    private void addListener(GpioPinDigitalInput input, SwitcherDevice device) {
        device.setListener(input);
        input.addListener((GpioPinListenerDigital) event -> {
            device.setState(event.getState().getValue() == 0 ? SwitcherState.OFF : SwitcherState.ON);
            // todo: send info to emmiter
        });
    }

    private GpioPinDigitalOutput createOutputPin(int address, String name) {
        return this.gpio.provisionDigitalOutputPin(RaspiPin.getPinByAddress(address), name);
    }

    private GpioPinDigitalInput createInputPin(int address) {
        return this.gpio.provisionDigitalInputPin(RaspiPin.getPinByAddress(address), PinPullResistance.PULL_DOWN);
    }
}
