package pl.piosdamian.homecontroller.interfaces.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import pl.piosdamian.homecontroller.application.communication.Observable;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/listener")
public class ObserversController {
    private final Observable observable;

    @GetMapping("/register")
    @CrossOrigin
    public ResponseEntity<SseEmitter> registerObserver(@RequestParam final String id) {
        return ResponseEntity.ok(this.observable.addObserver(id));
    }

    @PostMapping("/unregister")
    public ResponseEntity<Void> unregisterObserver(@RequestParam final String id) {
        this.observable.removeObserver(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
