package pl.piosdamian.homecontroller.application.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "gpio")
public class GpioConfiguration {

    private String configDirectoryPath = "";
    private Output output;

    @Data
    public static class Output {
        private long blinkDuration;
    }
}
