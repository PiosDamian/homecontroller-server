package pl.piosdamian.homecontroller.interfaces.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.piosdamian.homecontroller.application.scheduling.ScheduleService;
import pl.piosdamian.homecontroller.application.scheduling.dto.TaskDefinition;

import java.util.Collection;

@RestController
@RequestMapping("schedule")
@RequiredArgsConstructor
public class ScheduledTasksController {

    private final ScheduleService scheduleService;

    @PostMapping("task")
    public ResponseEntity<Void> createTask(@RequestBody final TaskDefinition taskDefinition) {
        this.scheduleService.registerTask(taskDefinition);
        return ResponseEntity.ok().build();
    }

    @GetMapping("tasks")
    public ResponseEntity<Collection<TaskDefinition>> getTasks() {
        return ResponseEntity.ok(this.scheduleService.getTasksList());
    }

    @DeleteMapping("task/{taskName}")
    public ResponseEntity<Void> removeTask(@PathVariable final String taskName) {
        this.scheduleService.removeTask(taskName);
        return ResponseEntity.ok().build();
    }
}
