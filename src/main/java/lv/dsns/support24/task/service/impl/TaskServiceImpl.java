package lv.dsns.support24.task.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lv.dsns.support24.common.dto.response.PageResponse;
import lv.dsns.support24.common.exception.ClientBackendException;
import lv.dsns.support24.common.exception.ErrorCode;
import lv.dsns.support24.common.smtp.EmailNotificationFactory;
import lv.dsns.support24.common.smtp.EmailNotificationService;
import lv.dsns.support24.problems.repository.ProblemRepository;
import lv.dsns.support24.problems.repository.entity.Problems;
import lv.dsns.support24.task.controller.dto.enums.Status;
import lv.dsns.support24.task.controller.dto.enums.Type;
import lv.dsns.support24.task.controller.dto.request.PatchByUserTaskRequestDTO;
import lv.dsns.support24.task.controller.dto.request.TaskRequestDTO;
import lv.dsns.support24.task.controller.dto.response.TaskResponseDTO;
import lv.dsns.support24.task.mapper.TaskMapper;
import lv.dsns.support24.task.repository.TaskRepository;
import lv.dsns.support24.task.repository.entity.Task;
import lv.dsns.support24.task.service.TaskService;
import lv.dsns.support24.task.service.filter.TaskFilter;
import lv.dsns.support24.unit.repository.UnitRepository;
import lv.dsns.support24.unit.repository.entity.Units;
import lv.dsns.support24.user.controller.dto.enums.Role;
import lv.dsns.support24.user.repository.SystemUsersRepository;
import lv.dsns.support24.user.repository.entity.SystemUsers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static lv.dsns.support24.common.specification.SpecificationCustom.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final UnitRepository unitRepository;
    private final TaskMapper tasksMapper;
    private final SystemUsersRepository usersRepository;
    private final EmailNotificationFactory emailNotificationFactory;

    @Override
    public List<TaskResponseDTO> findAll(TaskFilter taskFilter){
        var allTasks = taskRepository.findAll(getSearchSpecification(taskFilter));
        return allTasks.stream().map(tasksMapper::mapToDTO).collect(Collectors.toList());
    }
    @Override
    public List<TaskResponseDTO> findTaskById(UUID id){
        var taskById = taskRepository.findById(id);
        return taskById.stream().map(tasksMapper::mapToDTO).collect(Collectors.toList());
    }
    @Override
    public List<TaskResponseDTO> findAllSubtasks(UUID parentId){
        var allSubtasks = taskRepository.findAllSubtasks(parentId);
        return allSubtasks.stream().map(tasksMapper::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public PageResponse<TaskResponseDTO> findAllPageable(TaskFilter taskFilter, Pageable pageable) {
        // Retrieve the current user's UUID
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();

        UUID userUUID = usersRepository.findIdByEmail(email)
                .orElseThrow(() -> new ClientBackendException(ErrorCode.USER_NOT_FOUND));

        // Apply role-based filters
        if (userDetails.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            taskFilter.setAssignedForIds(Set.of(userUUID));
        }

        // Retrieve tasks with pagination and filtering
        Page<Task> allTasks = taskRepository.findAll(getSearchSpecification(taskFilter), pageable);

        // Convert to PageResponse
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
    public TaskResponseDTO save(TaskRequestDTO taskDTO) {
        // Convert DTO to entity and save
        var task = tasksMapper.mapToEntity(taskDTO);
        //set task type to SUBTASK if parent is set
        task.setTaskType(taskDTO.getParentId() != null ? Type.SUBTASK : Type.TASK);
        var savedTask = taskRepository.save(task);
        //send email notification
//        emailNotificationFactory.sendTaskCreatedNotification(savedTask);
        // Return the saved task DTO
        return tasksMapper.mapToDTO(savedTask);
    }


    @Override
    @Transactional
    public TaskResponseDTO patch (UUID id, TaskRequestDTO requestDTO){

        var taskById = taskRepository.findById(id)
                .orElseThrow(() -> new ClientBackendException(ErrorCode.TASK_NOT_FOUND));

        //send notification about assigning task
//        emailNotificationFactory.sendTaskAssignedNotification(requestDTO);

        taskById.setCompletedDate(requestDTO.getStatus() == Status.COMPLETED ? LocalDateTime.now() : null);

        tasksMapper.patchMerge(requestDTO, taskById);
        return tasksMapper.mapToDTO(taskById);
    }
    @Override
    @Transactional
    public TaskResponseDTO patchByUser (UUID id, PatchByUserTaskRequestDTO requestDTO){

        var taskById = taskRepository.findById(id)
                .orElseThrow(() -> new ClientBackendException(ErrorCode.TASK_NOT_FOUND));

        // Set the completedDate only if the status is COMPLETED
        if (requestDTO.getStatus() == Status.COMPLETED) {
            taskById.setCompletedDate(LocalDateTime.now());
        } else {
            taskById.setCompletedDate(null); // Clear the completedDate if the status is not COMPLETED
        }

        tasksMapper.patchMergeByUser(requestDTO,taskById);

        return tasksMapper.mapToDTO(taskById);
    }

    private Specification<Task> getSearchSpecification(TaskFilter taskFilter) {
        return Specification.where((Specification<Task>) searchLikeString("name", taskFilter.getSearch()))
                .and((Specification<Task>) searchFieldInCollectionOfIds("id", taskFilter.getIds()))
                .and((Specification<Task>) searchFieldInCollectionOfIds("assignedForId", taskFilter.getAssignedForIds()))
                .and((Specification<Task>) searchFieldInCollectionOfIds("assignedById", taskFilter.getAssignedByIds()))
                .and((Specification<Task>) searchFieldInCollectionOfIds("problemTypeId", taskFilter.getProblemTypeIds()))
                .and((Specification<Task>) searchFieldInCollectionOfIds("createdById", taskFilter.getCreatedByIds()))
                .and((Specification<Task>) searchOnStatus(taskFilter.getStatuses()))
                .and((Specification<Task>) searchOnPriority(taskFilter.getPriorities()))
                .and((Specification<Task>) searchOnTaskType(taskFilter.getTaskTypes()))
                .and((Specification<Task>) searchByDateRange("dueDate", taskFilter.getStartDate(), taskFilter.getEndDate()));
    }
}
