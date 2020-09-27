package pl.piosdamian.homecontroller.interfaces.rest.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StateChangeDTO {
    private int address;
    private SwitcherState state;
}
