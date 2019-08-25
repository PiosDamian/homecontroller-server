package pl.piosdamian.homecontroller.infractructure.rest.dto;

import lombok.Data;

@Data
public class SwitcherResponse {
    private int address;
    private String name;
    private String state;
}
