package lv.dsns.support24.task.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lv.dsns.support24.common.dto.response.PageResponse;
import lv.dsns.support24.common.exception.ClientBackendException;
import lv.dsns.support24.common.exception.ErrorCode;
import lv.dsns.support24.common.smtp.EmailNotificationFactory;
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
import lv.dsns.support24.user.controller.dto.enums.Role;
import lv.dsns.support24.user.repository.entity.SystemUsers;
import lv.dsns.support24.user.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static lv.dsns.support24.common.specification.SpecificationCustom.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper tasksMapper;
    private final UserService userService;
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
    public PageResponse<TaskResponseDTO> findAllPageable(Principal principal, TaskFilter taskFilter, Pageable pageable) {
        SystemUsers authUser = userService.getUserByEmail(principal.getName());

        // Apply role-based filters
        if (authUser.getRole().equals(Role.ROLE_ADMIN)){
            taskFilter.setAssignedForIds(Collections.singleton(authUser.getId()));
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
    public TaskResponseDTO save(Principal principal, TaskRequestDTO taskDTO) {
        // Convert DTO to entity and save
        var task = tasksMapper.mapToEntity(taskDTO);
        //set task type to SUBTASK if parent is set
        task.setTaskType(Objects.nonNull(taskDTO.getParentId()) ? Type.SUBTASK : Type.TASK);

        if (task.getTaskType().equals(Type.TASK)){
            //send email notification about created task
            emailNotificationFactory.sendTaskCreatedNotification(task);
        }else {
            var parentById = taskRepository.findById(task.getParentId())
                    .orElseThrow(() -> new ClientBackendException(ErrorCode.TASK_NOT_FOUND));

            task.setCreatedById(parentById.getCreatedById());

            task.setProblemTypeId(parentById.getProblemTypeId());

            SystemUsers byEmail = userService.getUserByEmail(principal.getName());

            task.setAssignedById(byEmail.getId());

            //send email notification about assigned task
            emailNotificationFactory.sendTaskAssignedNotification(task);

            task.setNotified(true);
        }
        var savedTask = taskRepository.save(task);

        // Return the saved task DTO
        return tasksMapper.mapToDTO(savedTask);
    }


    @Override
    @Transactional
    public TaskResponseDTO patch (Principal principal, UUID id, TaskRequestDTO requestDTO){

        var taskById = taskRepository.findById(id)
                .orElseThrow(() -> new ClientBackendException(ErrorCode.TASK_NOT_FOUND));

        tasksMapper.patchMerge(requestDTO, taskById);

        //add user comment to description
        if (Objects.nonNull(requestDTO.getComment())){
            setCommentToTask(principal, requestDTO.getComment(), taskById);
        }

        if (Objects.nonNull(requestDTO.getAssignedForId()) && Objects.isNull(taskById.getAssignedById())){
            SystemUsers byEmail = userService.getUserByEmail(principal.getName());

            taskById.setAssignedById(byEmail.getId());

            if (!taskById.isNotified()){
                emailNotificationFactory.sendTaskAssignedNotification(taskById);
                taskById.setNotified(true);
            }
        }

        return tasksMapper.mapToDTO(taskById);
    }
    @Override
    @Transactional
    public TaskResponseDTO patchByUser (Principal principal, UUID id, PatchByUserTaskRequestDTO requestDTO){

        var taskById = taskRepository.findById(id)
                .orElseThrow(() -> new ClientBackendException(ErrorCode.TASK_NOT_FOUND));

        // Set the completedDate only if the status is COMPLETED
        if (requestDTO.getStatus() == Status.COMPLETED) {
            taskById.setCompletedDate(LocalDateTime.now());
        } else {
            taskById.setCompletedDate(null); // Clear the completedDate if the status is not COMPLETED
        }

        //add user comment to description
        if (Objects.nonNull(requestDTO.getComment())){
            setCommentToTask(principal, requestDTO.getComment(),taskById);
        }

        tasksMapper.patchMergeByUser(requestDTO,taskById);

        return tasksMapper.mapToDTO(taskById);
    }

    @Override
    public void deleteTaskById(UUID id){
        var taskById = taskRepository.findById(id)
                .orElseThrow(() -> new ClientBackendException(ErrorCode.TASK_NOT_FOUND));

        taskRepository.delete(taskById);
    }

    private Specification<Task> getSearchSpecification(TaskFilter taskFilter) {
        return Specification.where((Specification<Task>) searchLikeString("description", taskFilter.getSearch()))
                .or((Specification<Task>) searchLikeStringWithJoin("assignedFor", "name", taskFilter.getSearch()))
                .or((Specification<Task>) searchLikeStringWithJoin("assignedBy", "name", taskFilter.getSearch()))
                .or((Specification<Task>) searchLikeStringWithJoin("createdBy", "name", taskFilter.getSearch()))
                .or((Specification<Task>) searchLikeStringWithJoin("taskProblem", "problem", taskFilter.getSearch()))
                .or((Specification<Task>) searchLikeStringWithTwoJoins("createdBy", "userUnit", "unitName", taskFilter.getSearch()))
                .and((Specification<Task>) searchFieldInCollectionOfIds("id", taskFilter.getIds()))
                .and((Specification<Task>) searchFieldInCollectionOfIds("assignedForId", taskFilter.getAssignedForIds()))
                .and((Specification<Task>) searchFieldInCollectionOfIds("assignedById", taskFilter.getAssignedByIds()))
                .and((Specification<Task>) searchFieldInCollectionOfIds("problemTypeId", taskFilter.getProblemTypeIds()))
                .and((Specification<Task>) searchFieldInCollectionOfIds("createdById", taskFilter.getCreatedByIds()))
                .and((Specification<Task>) searchOnField("status", taskFilter.getStatuses()))
                .and((Specification<Task>) searchOnField("priority",taskFilter.getPriorities()))
                .and((Specification<Task>) searchOnField("taskType",taskFilter.getTaskTypes()))
                .and((Specification<Task>) searchByDateRange("createdDate", taskFilter.getStartDate(), taskFilter.getEndDate()));
    }

    private void setCommentToTask(Principal principal, String comment, Task taskById){
        SystemUsers byEmail = userService.getUserByEmail(principal.getName());

        String description = String.format(
                "%s%n%n%s %s: %s",
                taskById.getDescription(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                byEmail.getName(),
                comment
        );

        taskById.setDescription(description);
    }
}
