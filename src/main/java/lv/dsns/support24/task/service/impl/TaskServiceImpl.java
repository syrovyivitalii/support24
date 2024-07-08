package lv.dsns.support24.task.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lv.dsns.support24.common.dto.response.PageResponse;
import lv.dsns.support24.common.exception.ClientBackendException;
import lv.dsns.support24.common.exception.ErrorCode;
import lv.dsns.support24.task.controller.dto.request.TaskRequestDTO;
import lv.dsns.support24.task.controller.dto.response.TaskResponseDTO;
import lv.dsns.support24.task.mapper.TaskMapper;
import lv.dsns.support24.task.repository.TasksRepository;
import lv.dsns.support24.task.repository.entity.Tasks;
import lv.dsns.support24.task.service.TaskService;
import lv.dsns.support24.task.service.filter.TaskFilter;
import lv.dsns.support24.user.repository.SystemUsersRepository;
import lv.dsns.support24.user.repository.entity.SystemUsers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


import java.util.*;
import java.util.stream.Collectors;

import static lv.dsns.support24.common.specification.SpecificationCustom.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TasksRepository tasksRepository;
    private final TaskMapper tasksMapper;

    private final SystemUsersRepository usersRepository;


    @Override
    public List<TaskResponseDTO> findAll(TaskFilter taskFilter){
        var allTasks = tasksRepository.findAll(getSearchSpecification(taskFilter));
        return allTasks.stream().map(tasksMapper::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public PageResponse<TaskResponseDTO> findAllPageable(TaskFilter taskFilter, Pageable pageable) {
        // Get the current authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String email = userDetails.getUsername();

        Optional<SystemUsers> byEmail = usersRepository.findByEmail(email);

        UUID userUUID = byEmail.get().getId();

        Page<Tasks> allTasks;

        if (userDetails.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_USER"))) {
            taskFilter.setCreatedForIds(Set.of(userUUID));
            allTasks = tasksRepository.findAll(getSearchSpecification(taskFilter), pageable);
        }else {
            allTasks = tasksRepository.findAll(getSearchSpecification(taskFilter), pageable);
        }
        List<TaskResponseDTO> taskDTOs = allTasks.stream()
                .map(tasksMapper::mapToDTO)
                .collect(Collectors.toList());

        return PageResponse.<TaskResponseDTO>builder()
                .totalPages((long) allTasks.getTotalPages())
                .pageSize((long) pageable.getPageSize())
                .totalElements(allTasks.getTotalElements())
                .content(taskDTOs)
                .build();
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

    private Specification<Tasks> getSearchSpecification(TaskFilter taskFilter) {
        return Specification.where((Specification<Tasks>) searchLikeString("name", taskFilter.getSearch()))
                .and((Specification<Tasks>) searchFieldInCollectionOfIds("id", taskFilter.getIds()))
                .and((Specification<Tasks>) searchFieldInCollectionOfIds("createdForId", taskFilter.getCreatedForIds()))
                .and((Specification<Tasks>) searchOnString("status", taskFilter.getStatus()))
                .and((Specification<Tasks>) searchByDueDate("dueDate", taskFilter.getDueDate()));
    }


}
