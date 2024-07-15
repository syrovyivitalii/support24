package lv.dsns.support24.task.service;

import lv.dsns.support24.common.dto.response.PageResponse;
import lv.dsns.support24.task.controller.dto.request.PatchByUserTaskRequestDTO;
import lv.dsns.support24.task.controller.dto.request.TaskRequestDTO;
import lv.dsns.support24.task.controller.dto.response.TaskResponseDTO;
import lv.dsns.support24.task.service.filter.TaskFilter;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.UUID;

public interface TaskService {
    List<TaskResponseDTO> findAll(TaskFilter taskFilter);

    PageResponse<TaskResponseDTO> findAllPageable(TaskFilter taskFilter, Pageable pageable);
    PageResponse<TaskResponseDTO> findAllCompletedPageable(TaskFilter taskFilter, Pageable pageable);
    PageResponse<TaskResponseDTO> findAllNotCompletedPageable(TaskFilter taskFilter, Pageable pageable);
    TaskResponseDTO save (TaskRequestDTO tasksDTO);

    TaskResponseDTO patch (UUID id, TaskRequestDTO requestDTO);

    TaskResponseDTO patchByUser (UUID id, PatchByUserTaskRequestDTO requestDTO);
}
