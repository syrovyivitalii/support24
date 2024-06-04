package lv.dsns.support24.task.service;

import lv.dsns.support24.task.controller.dto.request.TaskRequestDTO;
import lv.dsns.support24.task.controller.dto.response.TaskResponseDTO;

import java.util.List;
import java.util.UUID;

public interface TaskService {
    List<TaskResponseDTO> findAll();
    TaskResponseDTO save (TaskRequestDTO tasksDTO);

    TaskResponseDTO patch (UUID id, TaskRequestDTO requestDTO);
}
