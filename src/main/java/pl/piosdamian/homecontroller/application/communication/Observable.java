package pl.piosdamian.homecontroller.application.communication;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface Observable {
    /**
     * register new observer to notify
     * @return SseEmitter - opens push canal
     * @param id under which observer will be registered
     */
    SseEmitter addObserver(String id);

    /**
     * removes observer
     * @param id - id of observer to remove
     */
    void removeObserver(String id);
}
