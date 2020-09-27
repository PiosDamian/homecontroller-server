package pl.piosdamian.homecontroller.application.gpio;

import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit4.SpringRunner;
import pl.piosdamian.homecontroller.application.configuration.GpioConfiguration;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RunWith(SpringRunner.class)
@SpringBootTest
@Profile("rasp")
@Slf4j
public class GPIOControllerImplTest {

    @Autowired
    private GPIOController controller;

    @Autowired
    private GpioConfiguration gpioConfiguration;

    @Test
    @Ignore
    //manual test
    public void blink() throws IOException, InterruptedException {
        controller.registerSwitcher(29, "LED", true);
        controller.blink(29);
        Thread.sleep(this.gpioConfiguration.getOutput().getBlinkDuration());
    }
}