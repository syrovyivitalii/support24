package lv.dsns.support24.task.service.impl;

import lombok.extern.slf4j.Slf4j;
import lv.dsns.support24.task.controller.dto.request.TaskRequestDTO;
import lv.dsns.support24.task.controller.dto.response.TaskResponseDTO;
import lv.dsns.support24.task.mapper.TaskMapper;
import lv.dsns.support24.task.repository.TasksRepository;
import lv.dsns.support24.task.service.TaskService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TaskServiceImpl implements TaskService {
    private final TasksRepository tasksRepository;
    private final TaskMapper tasksMapper;

    public TaskServiceImpl(TasksRepository tasksRepository, TaskMapper tasksMapper) {
        this.tasksRepository = tasksRepository;
        this.tasksMapper = tasksMapper;
    }
    @Override
    public List<TaskResponseDTO> findAll(){
        var allTasks = tasksRepository.findAll();
        return allTasks.stream().map(tasksMapper::mapToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TaskResponseDTO save (TaskRequestDTO tasksDTO){
        var tasks = tasksMapper.mapToEntity(tasksDTO);

        var savedTask = tasksRepository.save(tasks);
        return tasksMapper.mapToDTO(savedTask);
    }
}
