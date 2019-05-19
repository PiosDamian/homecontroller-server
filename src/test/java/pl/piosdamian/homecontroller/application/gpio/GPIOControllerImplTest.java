package pl.piosdamian.homecontroller.application.gpio;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit4.SpringRunner;
import pl.piosdamian.homecontroller.application.configuration.GpioConfiguration;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
@Profile("rasp")
public class GPIOControllerImplTest {

    @Autowired
    private GPIOController controller;

    @Autowired
    private GpioConfiguration gpioConfiguration;

    @Test
    //manual test
    public void blink() throws IOException, InterruptedException {
        controller.registerSwitcher(29, "LED", true);
        controller.blink(29);
        Thread.sleep(this.gpioConfiguration.getOutput().getBlinkDuration());
    }
}