package lv.dsns.support24.task.controller;

import lv.dsns.support24.common.dto.response.PageResponse;
import lv.dsns.support24.task.controller.dto.request.PatchByUserTaskRequestDTO;
import lv.dsns.support24.task.controller.dto.request.TaskRequestDTO;
import lv.dsns.support24.task.controller.dto.response.TaskResponseDTO;
import lv.dsns.support24.task.service.filter.TaskFilter;
import lv.dsns.support24.task.service.impl.TaskServiceImpl;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
//@CrossOrigin(origins = "http://localhost:5173")
public class TaskController {

    private final TaskServiceImpl tasksService;

    public TaskController(TaskServiceImpl tasksService) {
        this.tasksService = tasksService;
    }

    @GetMapping("/private/tasks")
    public ResponseEntity<List<TaskResponseDTO>> getAllTasks(@ParameterObject TaskFilter taskFilter){
        var allTasks = tasksService.findAll(taskFilter);
        return ResponseEntity.ok(allTasks);
    }
    @GetMapping("/private/task/{id}")
    public ResponseEntity<List<TaskResponseDTO>> getTaskById(@PathVariable UUID id){
        var taskById = tasksService.findTaskById(id);
        return ResponseEntity.ok(taskById);
    }

    @GetMapping("/private/tasks/pageable")
    public ResponseEntity<PageResponse<TaskResponseDTO>> getAllTasksPageable(@ParameterObject Principal principal, @ParameterObject TaskFilter taskFilter, @SortDefault(sort = "createdDate", direction = Sort.Direction.DESC) @ParameterObject Pageable pageable){
        PageResponse<TaskResponseDTO> responseDTOS = tasksService.findAllPageable(principal,taskFilter,pageable);
        return ResponseEntity.ok(responseDTOS);
    }
    @GetMapping("/private/tasks/get-subtasks/{id}")
    public ResponseEntity<List<TaskResponseDTO>> getAllSubtask(@PathVariable UUID id){
        var allSubtasks = tasksService.findAllSubtasks(id);
        return ResponseEntity.ok(allSubtasks);
    }
    @PostMapping("/public/tasks")
    public ResponseEntity<TaskResponseDTO> save (@ParameterObject Principal principal, @RequestBody TaskRequestDTO tasksDTO){
        var saveTask = tasksService.save(principal,tasksDTO);
        return ResponseEntity.ok(saveTask);
    }
    @PatchMapping("/private/tasks/{id}")
    public ResponseEntity<TaskResponseDTO> patch(@ParameterObject Principal principal, @PathVariable UUID id, @RequestBody TaskRequestDTO requestDTO){
        var patchedTask = tasksService.patch(principal,id,requestDTO);

        return ResponseEntity.ok(patchedTask);
    }
    @PatchMapping("/private/tasks/by-user/{id}")
    public ResponseEntity<TaskResponseDTO> patchByUser(@PathVariable UUID id, @RequestBody PatchByUserTaskRequestDTO requestDTO){
        var patchedTask = tasksService.patchByUser(id,requestDTO);

        return ResponseEntity.ok(patchedTask);
    }
}
