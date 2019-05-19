package pl.piosdamian.homecontroller.application.gpio;

import pl.piosdamian.homecontroller.application.model.SwitcherDTO;

import java.io.IOException;
import java.util.List;

public interface GPIOController {
    void registerSwitcher(int pinAddress, String name, boolean force) throws IOException;
    void registerSwitcher(int pinAddress, String name, int listenerAddress, boolean force) throws IOException;
    void deleteSwitcher(int pinAddress) throws IOException;
    void updateListener(int switcherAddress, int listenerAddress) throws IOException;
    void updateName(int switcherAddress, String name) throws IOException;
    void blink(int address);
    List<SwitcherDTO> getDevices();
}
