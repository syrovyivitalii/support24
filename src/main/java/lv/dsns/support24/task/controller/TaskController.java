package lv.dsns.support24.task.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
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
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class TaskController {

    private final TaskServiceImpl tasksService;

    @GetMapping("/private/tasks")
    @Operation(summary = "Get all tasks with filters as list")
    public ResponseEntity<List<TaskResponseDTO>> getAllTasks(@ParameterObject TaskFilter taskFilter){
        var allTasks = tasksService.findAll(taskFilter);

        return ResponseEntity.ok(allTasks);
    }

    @GetMapping("/private/tasks/{id}")
    @Operation(summary = "Get task by id")
    public ResponseEntity<List<TaskResponseDTO>> getTaskById(@PathVariable UUID id){
        var taskById = tasksService.findTaskById(id);

        return ResponseEntity.ok(taskById);
    }

    @GetMapping("/private/tasks/pageable")
    @Operation(summary = "Get all tasks with filters, pageable, sorted by createdDate ")
    public ResponseEntity<PageResponse<TaskResponseDTO>> getAllTasksPageable(@ParameterObject Principal principal, @ParameterObject TaskFilter taskFilter, @SortDefault(sort = "createdDate", direction = Sort.Direction.DESC) @ParameterObject Pageable pageable){
        PageResponse<TaskResponseDTO> allTasks = tasksService.findAllPageable(principal,taskFilter,pageable);

        return ResponseEntity.ok(allTasks);
    }

    @GetMapping("/private/tasks/get-subtasks/{parentTaskId}")
    @Operation(summary = "Get all subtasks by parent id")
    public ResponseEntity<List<TaskResponseDTO>> getAllSubtask(@PathVariable UUID parentTaskId){
        var allSubtasks = tasksService.findAllSubtasks(parentTaskId);

        return ResponseEntity.ok(allSubtasks);
    }

    @PostMapping("/public/tasks")
    @Operation(summary = "Save task")
    public ResponseEntity<TaskResponseDTO> save (@ParameterObject Principal principal, @RequestBody TaskRequestDTO tasksDTO){
        var saveTask = tasksService.save(principal,tasksDTO);

        return ResponseEntity.ok(saveTask);
    }

    @PatchMapping("/private/tasks/{id}")
    @Operation(summary = "Patch task")
    public ResponseEntity<TaskResponseDTO> patch(@ParameterObject Principal principal, @PathVariable UUID id, @RequestBody TaskRequestDTO requestDTO){
        var patchedTask = tasksService.patch(principal,id,requestDTO);

        return ResponseEntity.ok(patchedTask);
    }

    @PatchMapping("/private/tasks/by-user/{id}")
    @Operation(summary = "Patch task by user")
    public ResponseEntity<TaskResponseDTO> patchByUser(@ParameterObject Principal principal, @PathVariable UUID id, @RequestBody PatchByUserTaskRequestDTO requestDTO){
        var patchedTask = tasksService.patchByUser(principal, id, requestDTO);

        return ResponseEntity.ok(patchedTask);
    }
}
