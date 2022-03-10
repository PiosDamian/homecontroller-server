package pl.piosdamian.homecontroller.application.scheduling.storing;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import pl.piosdamian.homecontroller.application.configuration.GpioConfiguration;
import pl.piosdamian.homecontroller.application.scheduling.dto.TaskDefinition;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static pl.piosdamian.homecontroller.application.utils.Mapper.JSON_MAPPER;

@Service
@Profile("rasp")
@Slf4j
public class RaspSchedulTasksRepository extends AbstractScheduleTasksRepository {
    private static final String SERIALIZERS_FILE = "serializers.json";
    private final File schedulersFile;

    public RaspSchedulTasksRepository(final GpioConfiguration gpioConfiguration) {
        this.schedulersFile = new File(gpioConfiguration.getConfigDirectoryPath() + File.separator + RaspSchedulTasksRepository.SERIALIZERS_FILE);
        if (!this.schedulersFile.getParentFile().exists()) {
            this.schedulersFile.getParentFile().mkdirs();
        }
    }

    @Override
    protected void storeSchedulers() {
        try (final FileOutputStream fos = new FileOutputStream(this.schedulersFile)) {
            fos.write(JSON_MAPPER.writeValueAsBytes(this.tasks));
        } catch (FileNotFoundException nfe) {
            log.warn("Cannot find schedulers file: {}", nfe.getMessage());
        } catch (IOException io) {
            log.warn("Problem with writing to schedulers file: {}", io.getMessage());
        }
    }

    @Override
    protected Map<String, TaskDefinition> readConfiguration() throws IOException {
        if (this.schedulersFile.exists()) {
            try (final FileInputStream fis = new FileInputStream(this.schedulersFile)) {
                return JSON_MAPPER.readValue(fis, new TypeReference<>() {
                });
            } catch (IOException e) {
                log.warn("Can not read schedulers configuration file");
                throw e;
            }
        } else {
            return new HashMap<>();
        }
    }
}
