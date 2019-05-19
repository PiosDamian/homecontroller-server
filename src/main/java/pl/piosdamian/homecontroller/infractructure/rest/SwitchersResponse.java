package pl.piosdamian.homecontroller.infractructure.rest;

import lombok.Data;

@Data
public class SwitchersResponse {
    private int address;
    private String name;
    private String state;
}
