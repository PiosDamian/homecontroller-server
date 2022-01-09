package pl.piosdamian.homecontroller.application.gpio;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import pl.piosdamian.homecontroller.application.communication.Broadcaster;
import pl.piosdamian.homecontroller.application.configuration.GpioConfiguration;
import pl.piosdamian.homecontroller.application.model.SwitcherDevice;
import pl.piosdamian.homecontroller.application.serialization.PinsConfiguration;
import pl.piosdamian.homecontroller.interfaces.rest.dto.response.StateChangeDTO;
import pl.piosdamian.homecontroller.interfaces.rest.dto.response.SwitcherDTO;
import pl.piosdamian.homecontroller.interfaces.rest.dto.response.SwitcherState;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@Profile("rasp")
public class GPIOControllerImpl implements GPIOController {
    private final GpioController gpio;
    private final GpioConfiguration gpioConfiguration;
    private final PinsConfiguration pinsConfiguration;
    private final Broadcaster broadcaster;
    private final Set<Integer> reservedPins = new HashSet<>();

    private final Map<Integer, SwitcherDevice> switcherDevices = new HashMap<>();

    public GPIOControllerImpl(
            final GpioConfiguration gpioConfiguration,
            final PinsConfiguration pinsConfiguration,
            final Broadcaster broadcaster) throws IOException {
        this.gpioConfiguration = gpioConfiguration;
        this.pinsConfiguration = pinsConfiguration;
        this.broadcaster = broadcaster;
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

    @Override
    public void deleteSwitcher(int pinAddress) throws IOException {
        SwitcherDevice switcherDevice = this.switcherDevices.remove(pinAddress);
        this.gpio.unprovisionPin(switcherDevice.getBlinker());
        removeListeners(switcherDevice);
        pinsConfiguration.serializePins(this.switcherDevices);
        this.reservedPins.remove(pinAddress);
    }

    @Override
    public void updateListener(int switcherAddress, int listenerAddress) throws IOException {
        Optional.ofNullable(this.switcherDevices.get(switcherAddress)).ifPresent(switcher -> {
            removeListeners(switcher);
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
        Optional.ofNullable(switcherDevices.get(address)).map(device -> {
                    new Thread(() ->
                            device
                                    .getBlinker()
                                    .pulse(gpioConfiguration.getOutput().getBlinkDuration())).start();
                    return device;
                }
        ).orElseThrow();

    }

    @Override
    public Collection<SwitcherDTO> getSwitchers() {
        return this.switcherDevices
                .entrySet()
                .stream()
                .map(entry -> new SwitcherDTO(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    @Override
    public Set<Integer> getReservedPins() {
        return this.reservedPins;
    }

    private SwitcherDevice createSwitcherDevice(int pinAddress, String name, boolean force) {
        if (switcherDevices.containsKey(pinAddress) && !force) {
            throw new GpioControllerException("Pin already in use");
        }
        if (switcherDevices.containsKey(pinAddress)) {
            final SwitcherDevice switcherDevice = switcherDevices.get(pinAddress);
            removeListeners(switcherDevice);
            this.gpio.unprovisionPin(switcherDevice.getBlinker());
        }
        SwitcherDevice switcherDevice = new SwitcherDevice();
        switcherDevice.setBlinker(createOutputPin(pinAddress, name));
        switcherDevice.setName(name);
        switcherDevices.put(pinAddress, switcherDevice);

        this.reservedPins.add(pinAddress);

        return switcherDevice;
    }

    private void createSwitcherWithListener(int pinAddress, String name, int listenerAddress, boolean force) {
        SwitcherDevice switcherDevice = createSwitcherDevice(pinAddress, name, force);
        addListener(createInputPin(listenerAddress), switcherDevice);
    }

    private void addListener(GpioPinDigitalInput input, SwitcherDevice device) {
        device.setListener(input);
        input.addListener((GpioPinListenerDigital) event -> {
            final SwitcherState state = event.getState().getValue() == 0 ? SwitcherState.OFF : SwitcherState.ON;
            device.setState(state);
            final StateChangeDTO stateChangeDTO =
                    new StateChangeDTO(device.getBlinker().getPin().getAddress(), device.getState());
            broadcaster.next(new PushMessage("stateUpdate", stateChangeDTO));
        });
        this.reservedPins.add(input.getPin().getAddress());
    }

    private void removeListeners(SwitcherDevice switcherDevice) {
        Optional.ofNullable(switcherDevice.getListener()).ifPresent(pin -> {
            pin.removeAllListeners();
            this.gpio.unprovisionPin(pin);
            this.reservedPins.remove(pin.getPin().getAddress());
        });
    }

    private GpioPinDigitalOutput createOutputPin(int address, String name) {
        final GpioPinDigitalOutput pin = this.gpio.provisionDigitalOutputPin(RaspiPin.getPinByAddress(address), name);
        this.setShutdownMode(pin);
        return pin;
    }

    private GpioPinDigitalInput createInputPin(int address) {
        final GpioPinDigitalInput pin = this.gpio.provisionDigitalInputPin(RaspiPin.getPinByAddress(address), PinPullResistance.PULL_DOWN);
        this.setShutdownMode(pin);
        return pin;
    }

    private void setShutdownMode(GpioPin pin) {
        pin.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
    }

    @RequiredArgsConstructor
    @Getter
    public static class PushMessage {
        private final String type;
        private final Object payload;
    }
}
