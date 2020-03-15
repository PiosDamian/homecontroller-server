package pl.piosdamian.homecontroller.application.communication;

import org.springframework.lang.Nullable;

public interface Broadcaster {
    /**
     * sends new event
     * @param data data to send
     */
    void next(@Nullable Object data);
}
