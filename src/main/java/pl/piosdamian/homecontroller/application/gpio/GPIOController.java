package pl.piosdamian.homecontroller.application.gpio;

import pl.piosdamian.homecontroller.application.model.SwitcherDTO;

import java.util.List;

public interface GPIOController {
    void registerSwitcher(int pinAddress, String name);
    void registerSwitcher(int pinAddress, String name, int listenerAddress);
    void deleteSwitcher(int pinAddress);
    void updateListener(int switcherAddress, int listenerAddress);
    void updateName(int switcherAddress, String name);
    void blink(int address);
    List<SwitcherDTO> getDevices();
}
