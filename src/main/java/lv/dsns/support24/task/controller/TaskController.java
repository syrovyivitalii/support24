package lv.dsns.support24.task.controller;

import lv.dsns.support24.common.dto.response.PageResponse;
import lv.dsns.support24.task.controller.dto.request.TaskRequestDTO;
import lv.dsns.support24.task.controller.dto.response.TaskResponseDTO;
import lv.dsns.support24.task.service.filter.TaskFilter;
import lv.dsns.support24.task.service.impl.TaskServiceImpl;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tasks")
@CrossOrigin(origins = "http://localhost:5173")
public class TaskController {

    private final TaskServiceImpl tasksService;

    public TaskController(TaskServiceImpl tasksService) {
        this.tasksService = tasksService;
    }

    @GetMapping
    public ResponseEntity<List<TaskResponseDTO>> getAllTasks(@ParameterObject TaskFilter taskFilter){
        var allTasks = tasksService.findAll(taskFilter);
        return ResponseEntity.ok(allTasks);
    }

    @GetMapping("/pageable")
    public ResponseEntity<PageResponse<TaskResponseDTO>> getAllTasksPageable(@ParameterObject TaskFilter taskFilter, @ParameterObject Pageable pageable){
        PageResponse<TaskResponseDTO> responseDTOS = tasksService.findAllPageable(taskFilter,pageable);
        return ResponseEntity.ok(responseDTOS);
    }

    @PostMapping
    public ResponseEntity<TaskResponseDTO> save (@RequestBody TaskRequestDTO tasksDTO){
        var saveTask = tasksService.save(tasksDTO);
        return ResponseEntity.ok(saveTask);
    }
    @PatchMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> patch(@PathVariable UUID id, @RequestBody TaskRequestDTO requestDTO){
        var patchedTask = tasksService.patch(id,requestDTO);

        return ResponseEntity.ok(patchedTask);
    }
}
