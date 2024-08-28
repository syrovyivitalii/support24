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
import lv.dsns.support24.user.repository.SystemUsersRepository;
import lv.dsns.support24.user.repository.entity.SystemUsers;
import lv.dsns.support24.user.service.UserService;
import lv.dsns.support24.user.service.impl.UserServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


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
    private final SystemUsersRepository usersRepository;
    private final UserServiceImpl userService;
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
        //get email of user
        String email = userService.getAuthenticatedUserEmail();

        UUID userUUID = usersRepository.findIdByEmail(email)
                .orElseThrow(() -> new ClientBackendException(ErrorCode.USER_NOT_FOUND));

        Collection<? extends GrantedAuthority> authenticatedUserAuthority = userService.getAuthenticatedUserAuthority();

        // Apply role-based filters
        if (authenticatedUserAuthority.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
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

        if (task.getTaskType().equals(Type.TASK)){
            //send email notification about created task
            emailNotificationFactory.sendTaskCreatedNotification(task);
        }else {
            var parentById = taskRepository.findById(task.getParentId())
                    .orElseThrow(() -> new ClientBackendException(ErrorCode.TASK_NOT_FOUND));
            task.setCreatedById(parentById.getCreatedById());
            task.setProblemTypeId(parentById.getProblemTypeId());
            var byEmail = usersRepository.findByEmail(userService.getAuthenticatedUserEmail())
                    .orElseThrow(() -> new ClientBackendException(ErrorCode.USER_NOT_FOUND));
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
    public TaskResponseDTO patch (UUID id, TaskRequestDTO requestDTO){

        var taskById = taskRepository.findById(id)
                .orElseThrow(() -> new ClientBackendException(ErrorCode.TASK_NOT_FOUND));

        tasksMapper.patchMerge(requestDTO, taskById);

        //add user comment to description
        if (requestDTO.getComment() != null){
            setCommentToTask(requestDTO.getComment(),taskById);
        }

        if (requestDTO.getAssignedForId() != null){
            var byEmail = usersRepository.findByEmail(userService.getAuthenticatedUserEmail())
                    .orElseThrow(() -> new ClientBackendException(ErrorCode.USER_NOT_FOUND));
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
    public TaskResponseDTO patchByUser (UUID id, PatchByUserTaskRequestDTO requestDTO){

        var taskById = taskRepository.findById(id)
                .orElseThrow(() -> new ClientBackendException(ErrorCode.TASK_NOT_FOUND));

        // Set the completedDate only if the status is COMPLETED
        if (requestDTO.getStatus() == Status.COMPLETED) {
            taskById.setCompletedDate(LocalDateTime.now());
        } else {
            taskById.setCompletedDate(null); // Clear the completedDate if the status is not COMPLETED
        }

        //add user comment to description
        if (requestDTO.getComment() != null){
            setCommentToTask(requestDTO.getComment(),taskById);
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
                .and((Specification<Task>) searchByDateRange("createdDate", taskFilter.getStartDate(), taskFilter.getEndDate()));
    }

    private void setCommentToTask(String comment, Task taskById){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();

        var byEmail = usersRepository.findByEmail(email)
                .orElseThrow(() -> new ClientBackendException(ErrorCode.USER_NOT_FOUND));

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
