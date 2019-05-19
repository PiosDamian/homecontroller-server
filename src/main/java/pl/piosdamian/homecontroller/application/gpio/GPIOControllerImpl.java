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

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class GPIOControllerImpl implements GPIOController {
    private final GpioController gpio;
    private final GpioConfiguration gpioConfiguration;

    private Map<Integer, SwitcherDevice> switcherDevices = new HashMap<>();

    public GPIOControllerImpl(GpioConfiguration gpioConfiguration) {
        this.gpioConfiguration = gpioConfiguration;
        GpioController tmp;
        try {
            tmp = GpioFactory.getInstance();
        } catch (Throwable t) {
            log.warn("Platform not supported, {}", t.getMessage());
            tmp = null;
        }
        this.gpio = tmp;
    }

    public void registerSwitcher(int pinAddress, String name) {
        createSwitcherDevice(pinAddress, name);
    }

    public void registerSwitcher(int pinAddress, String name, int listenerAddress) {
        SwitcherDevice switcherDevice = createSwitcherDevice(pinAddress, name);
        addListener(createInputPin(listenerAddress), switcherDevice);
    }

    @Override
    public void deleteSwitcher(int pinAddress) {
        SwitcherDevice switcherDevice = this.switcherDevices.remove(pinAddress);
        Optional.ofNullable(switcherDevice.getListener()).ifPresent(GpioPin::removeAllListeners);
    }

    @Override
    public void updateListener(int switcherAddress, int listenerAddress) {
        Optional.of(this.switcherDevices.get(switcherAddress)).ifPresent(switcher -> {
            Optional.ofNullable(switcher.getListener()).ifPresent(GpioPin::removeAllListeners);
            addListener(createInputPin(listenerAddress), switcher);
        });
    }

    @Override
    public void updateName(int switcherAddress, String name) {
        this.switcherDevices.get(switcherAddress).setName(name);
    }

    public void blink(int address) {
        new Thread(() ->
                switcherDevices
                        .get(address)
                        .getBlinker()
                        .pulse(gpioConfiguration.getOutput().getBlinkDuration()))
                .run();
    }

    @Override
    public List<SwitcherDTO> getDevices() {
        return this.switcherDevices
                .entrySet()
                .stream()
                .map(entry -> new SwitcherDTO(entry.getKey(), entry.getValue().getName(), entry.getValue().getState().name()))
                .collect(Collectors.toList());
    }

    private SwitcherDevice createSwitcherDevice(int pinAddress, String name) {
        SwitcherDevice switcherDevice = new SwitcherDevice();
        switcherDevice.setBlinker(gpio.provisionDigitalOutputPin(RaspiPin.getPinByAddress(pinAddress), name, PinState.LOW));
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
