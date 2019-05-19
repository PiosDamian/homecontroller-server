package pl.piosdamian.homecontroller.application.gpio;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Profile("rasp")
public class GPIOControllerImplTest {

    @Autowired
    private GPIOController controller;

    @Test
    @Ignore
    //manual test
    public void blink() throws IOException {
        controller.registerSwitcher(16, "LED", true);
        controller.blink(16);
    }
}