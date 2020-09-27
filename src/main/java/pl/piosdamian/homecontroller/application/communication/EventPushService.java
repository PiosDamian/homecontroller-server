package pl.piosdamian.homecontroller.application.communication;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.*;

@Slf4j
@Service
public class EventPushService implements Observable, Broadcaster {

	private final Map<String, SseEmitter> observers = new HashMap<>();
	private final List<String> deadObservers = new ArrayList<>();
	@Value("${emitter.source.timeout: 3600000}")
	private long sseEmitterTimeout;

	@Override
	public void next(Object data) {
		final Set<ResponseBodyEmitter.DataWithMediaType> build = SseEmitter
				.event()
				.data(data, MediaType.APPLICATION_JSON).build();
		this.observers.forEach((id, emitter) -> {
			try {
				emitter.send(data);
			} catch (IOException e) {
				log.warn("Emitter with id {} thrown IOException: {}, will be removed", id, e.getMessage());
				this.deadObservers.add(id);
			}
		});
		this.cleanDeadObservers();
	}

	@Override
	public SseEmitter addObserver(String id) {
		final SseEmitter emitter = new SseEmitter(this.sseEmitterTimeout);
		emitter.onCompletion(() -> this.removeObserver(id));
		emitter.onTimeout(() -> {
			log.warn("Timeout occurred for observer {}", id);
			this.removeObserver(id);
		});
		emitter.onError((error) -> {
			log.warn(error.getMessage());
			this.removeObserver(id);
		});
		this.observers.put(id, emitter);
		return emitter;
	}

	@Override
	public void removeObserver(String id) {
		Optional.ofNullable(this.observers.remove(id)).ifPresent(ResponseBodyEmitter::complete);
	}

	private void cleanDeadObservers() {
		this.deadObservers.forEach(this::removeObserver);
		this.deadObservers.clear();
	}
}
