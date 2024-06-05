package lv.dsns.support24.task.service.impl;

import lombok.extern.slf4j.Slf4j;
import lv.dsns.support24.common.exception.ClientBackendException;
import lv.dsns.support24.common.exception.ErrorCode;
import lv.dsns.support24.task.controller.dto.request.TaskRequestDTO;
import lv.dsns.support24.task.controller.dto.response.TaskResponseDTO;
import lv.dsns.support24.task.mapper.TaskMapper;
import lv.dsns.support24.task.repository.TasksRepository;
import lv.dsns.support24.task.service.TaskService;
import lv.dsns.support24.task.service.filter.TaskFilter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
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

    @Override
    @Transactional
    public TaskResponseDTO patch (UUID id, TaskRequestDTO requestDTO){
        var taskById = tasksRepository.findById(id)
                .orElseThrow(() -> new ClientBackendException(ErrorCode.TASK_NOT_FOUND));

        tasksMapper.patchMerge(requestDTO,taskById);

        var savedTask = tasksRepository.save(taskById);

        return tasksMapper.mapToDTO(savedTask);
    }

//    private Specification<Task> getSearchSpecification(TaskFilter taskFilter) {
//        return Specification.where((Specification<Task>) searchLikeString("teamName", taskFilter.getSearch()))
//                .and((Specification<Task>) searchFieldInCollectionOfIds("reference", taskFilter.getIds()))
//                .and((Specification<Task>) searchFieldInCollectionOfJoinedIds("teamCompetitions", "competition", "reference", taskFilter.getCompetitionReferences()));
//    }


}
