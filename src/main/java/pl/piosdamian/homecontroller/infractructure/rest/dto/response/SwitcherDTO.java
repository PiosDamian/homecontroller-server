package pl.piosdamian.homecontroller.infractructure.rest.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SwitcherDTO {
    private int address;
    private String name;
    private SwitcherState state;
}
