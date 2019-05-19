package pl.piosdamian.homecontroller.application.gpio;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GPIOControllerImplTest {

    @Autowired
    private GPIOController controller;

    @Test
    @Ignore
    //manual test
    public void blink() {
        controller.registerSwitcher(16, "LED");
        controller.blink(16);
    }
}