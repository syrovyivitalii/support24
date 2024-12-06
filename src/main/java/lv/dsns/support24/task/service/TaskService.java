package lv.dsns.support24.task.service;

import lv.dsns.support24.common.dto.response.PageResponse;
import lv.dsns.support24.task.controller.dto.request.PatchByUserTaskRequestDTO;
import lv.dsns.support24.task.controller.dto.request.TaskRequestDTO;
import lv.dsns.support24.task.controller.dto.response.TaskResponseDTO;
import lv.dsns.support24.task.service.filter.TaskFilter;
import org.springframework.data.domain.Pageable;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

public interface TaskService {

    List<TaskResponseDTO> findAll(TaskFilter taskFilter);

    List<TaskResponseDTO> findTaskById(UUID id);

    PageResponse<TaskResponseDTO> findAllPageable(Principal principal, TaskFilter taskFilter, Pageable pageable);

    List<TaskResponseDTO> findAllSubtasks(UUID parentId);

    TaskResponseDTO save (Principal principal, TaskRequestDTO tasksDTO);

    TaskResponseDTO patch (Principal principal, UUID id, TaskRequestDTO requestDTO);

    TaskResponseDTO patchByUser (Principal principal, UUID id, PatchByUserTaskRequestDTO requestDTO);
}
