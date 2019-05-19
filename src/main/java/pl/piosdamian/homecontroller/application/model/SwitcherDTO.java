package pl.piosdamian.homecontroller.application.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SwitcherDTO {
    private int address;
    private String name;
    private String state;
}
