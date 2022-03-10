package pl.piosdamian.homecontroller.application.scheduling.storing;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import pl.piosdamian.homecontroller.application.scheduling.dto.TaskDefinition;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@Profile("!rasp")
public class MockScheduledTasksRepositoryImpl extends AbstractScheduleTasksRepository {
    @Override
    protected void storeSchedulers() {
    }

    @Override
    protected Map<String, TaskDefinition> readConfiguration() throws IOException {
        return new HashMap<>();
    }
}
