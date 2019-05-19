package pl.piosdamian.homecontroller.application.gpio;

public class GpioControllerException extends RuntimeException {
    public GpioControllerException() {
        super();
    }

    public GpioControllerException(String msg) {
        super(msg);
    }
}
