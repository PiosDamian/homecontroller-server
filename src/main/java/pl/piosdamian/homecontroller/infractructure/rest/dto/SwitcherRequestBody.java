package pl.piosdamian.homecontroller.infractructure.rest.dto;

import lombok.Data;

@Data
public class SwitcherRequestBody {
    private Integer pinAddress;
    private String name;
    private Integer listenerAddress;
    private boolean force;
}
