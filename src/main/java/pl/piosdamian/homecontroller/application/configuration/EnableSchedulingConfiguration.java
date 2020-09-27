package pl.piosdamian.homecontroller.application.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@ConditionalOnProperty("service.enableScheduling")
public class EnableSchedulingConfiguration {
}
