package pl.piosdamian.homecontroller.infractructure.rest;

import lombok.Data;

@Data
public class SwitcherRequestBody {
    private Integer pinAddress;
    private String name;
    private Integer listenerAddress;
}
