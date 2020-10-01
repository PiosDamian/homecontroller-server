package pl.piosdamian.homecontroller.interfaces.rest.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.piosdamian.homecontroller.application.model.SwitcherDevice;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SwitcherDTO {
    private int address;
    private String name;
    private SwitcherState state;
    private Integer listenerAddress;

    public SwitcherDTO(final int address, final SwitcherDevice device) {
        this.address = address;
        this.name = device.getName();
        this.state = device.getState();
        try {
            this.listenerAddress = device.getListener().getPin().getAddress();
        } catch (Exception e) {
            this.listenerAddress = null;
        }
    }
}
