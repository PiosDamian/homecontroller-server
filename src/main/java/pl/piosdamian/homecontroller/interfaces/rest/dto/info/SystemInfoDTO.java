package pl.piosdamian.homecontroller.interfaces.rest.dto.info;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "core")
public class SystemInfoDTO {
	private String version;
}
