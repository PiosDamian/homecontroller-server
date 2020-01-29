package pl.piosdamian.homecontroller.infractructure.rest.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * used for updating {@link pl.piosdamian.homecontroller.application.model.SwitcherDevice}
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestSwitcherDTO {
    /**
     * name of switcher
     */
    private String name;
    /**
     * physical address of pin to which listener device is connected
     */
    private Integer listenerAddress;
    /**
     * used to override actual {@link pl.piosdamian.homecontroller.application.model.SwitcherDevice} with new
     * if pin have been connected with one
     * Optional
     */
    private boolean force = false;
}
