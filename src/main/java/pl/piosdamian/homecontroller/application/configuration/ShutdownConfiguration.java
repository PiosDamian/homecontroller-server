package pl.piosdamian.homecontroller.application.configuration;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("rasp")
@Slf4j
public class ShutdownConfiguration {

    @Bean(destroyMethod = "destroy")
    public PreDestroyBean preDestroyBean() {
        return new PreDestroyBean();
    }

    private static class PreDestroyBean {
        private final GpioController gpio;

        private PreDestroyBean() {
            this.gpio = GpioFactory.getInstance();
        }

        public final void destroy() {
            this.gpio.shutdown();
        }
    }
}
