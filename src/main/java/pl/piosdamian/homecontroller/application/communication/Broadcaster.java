package pl.piosdamian.homecontroller.application.communication;

import org.springframework.lang.Nullable;

public interface Broadcaster {
    /**
     * sends new event
     * @param eventName name of event to sent
     * @param data data to send
     */
    void next(String eventName, @Nullable Object data);

    /**
     * send signal about new event without data
     * @param eventName name of event
     */
    default void next(String eventName) {
        this.next(eventName, null);
    }
}
