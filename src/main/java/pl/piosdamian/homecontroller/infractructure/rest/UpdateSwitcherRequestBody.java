package pl.piosdamian.homecontroller.infractructure.rest;

import lombok.Data;

@Data
public class UpdateSwitcherRequestBody {
    private String name;
    private Integer listenerAddress;
}
