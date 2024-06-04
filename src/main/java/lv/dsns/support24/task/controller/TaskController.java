package lv.dsns.support24.task.controller;

import lv.dsns.support24.task.controller.dto.request.TaskRequestDTO;
import lv.dsns.support24.task.controller.dto.response.TaskResponseDTO;
import lv.dsns.support24.task.service.impl.TaskServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/task")
public class TaskController {

    private final TaskServiceImpl tasksService;

    public TaskController(TaskServiceImpl tasksService) {
        this.tasksService = tasksService;
    }

    @GetMapping
    public ResponseEntity<List<TaskResponseDTO>> getAllTasks(){
        var allTasks = tasksService.findAll();
        return ResponseEntity.ok(allTasks);
    }

    @PostMapping
    public ResponseEntity<TaskResponseDTO> save (@RequestBody TaskRequestDTO tasksDTO){
        var saveTask = tasksService.save(tasksDTO);
        return ResponseEntity.ok(saveTask);
    }
    @PatchMapping
    public ResponseEntity<TaskResponseDTO> patch(@PathVariable UUID id, @RequestBody TaskRequestDTO requestDTO){
        var patchedTask = tasksService.patch(id,requestDTO);

        return ResponseEntity.ok(patchedTask);
    }
}
