package lv.dsns.support24.task.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lv.dsns.support24.common.dto.response.PageResponse;
import lv.dsns.support24.common.exception.ClientBackendException;
import lv.dsns.support24.common.exception.ErrorCode;
import lv.dsns.support24.common.smtp.EmailNotificationService;
import lv.dsns.support24.problems.repository.ProblemRepository;
import lv.dsns.support24.problems.repository.entity.Problems;
import lv.dsns.support24.task.controller.dto.enums.Status;
import lv.dsns.support24.task.controller.dto.request.PatchByUserTaskRequestDTO;
import lv.dsns.support24.task.controller.dto.request.TaskRequestDTO;
import lv.dsns.support24.task.controller.dto.response.TaskResponseDTO;
import lv.dsns.support24.task.mapper.TaskMapper;
import lv.dsns.support24.task.repository.TasksRepository;
import lv.dsns.support24.task.repository.entity.Tasks;
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
    private final TasksRepository tasksRepository;
    private final UnitRepository unitRepository;
    private final TaskMapper tasksMapper;
    private final SystemUsersRepository usersRepository;
    private final ProblemRepository problemRepository;
    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

    private final EmailNotificationService emailNotificationService;

    @Override
    public List<TaskResponseDTO> findAll(TaskFilter taskFilter){
        var allTasks = tasksRepository.findAll(getSearchSpecification(taskFilter));
        return allTasks.stream().map(tasksMapper::mapToDTO).collect(Collectors.toList());
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
        Page<Tasks> allTasks = tasksRepository.findAll(getSearchSpecification(taskFilter), pageable);

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
        var savedTask = tasksRepository.save(task);

        SystemUsers userById = usersRepository.findById(savedTask.getCreatedById())
                .orElseThrow(() -> new ClientBackendException(ErrorCode.USER_NOT_FOUND, "Assigned by user not found"));
        Units unitById = unitRepository.findById(userById.getUnitId())
                .orElseThrow(() -> new ClientBackendException(ErrorCode.UNIT_NOT_FOUND));
        Problems problemById = problemRepository.findById(savedTask.getProblemTypeId())
                .orElseThrow(() -> new ClientBackendException(ErrorCode.PROBLEM_NOT_FOUND));

        List<String> optionalEmails = usersRepository.findEmailsByRole(Role.ROLE_SUPER_ADMIN);

        // If no emails found, use a default recipient or handle the situation
        if (optionalEmails.isEmpty()) {
            optionalEmails.add("v.syrovyi@dsns.gov.ua");
        }
        // Prepare properties for email template
        Map<String, Object> properties = new HashMap<>();
        properties.put("userName", userById.getName()); // Assume the Task entity has a 'getTitle()' method
        properties.put("union", unitById.getUnitName());
        properties.put("taskDescription", savedTask.getDescription()); // Assume the Task entity has a 'getDescription()' method
        properties.put("typeProblem",problemById.getProblem());

        // Send email notification to all recipients
        for (String recipient : optionalEmails) {
            emailNotificationService.sendNotification(
                    recipient,
                    "Нове звернення з проблемою!", // Subject
                    properties,
                    "new-task-email.ftl" // Ensure you have a corresponding template
            );
        }

        // Return the saved task DTO
        return tasksMapper.mapToDTO(savedTask);
    }


    @Override
    @Transactional
    public TaskResponseDTO patch (UUID id, TaskRequestDTO requestDTO){

        var taskById = tasksRepository.findById(id)
                .orElseThrow(() -> new ClientBackendException(ErrorCode.TASK_NOT_FOUND));

        if (requestDTO.getAssignedForId() != null && !requestDTO.isNotified()) {
            String assignedForEmail = usersRepository.findEmailById(requestDTO.getAssignedForId())
                    .orElseThrow(() -> new ClientBackendException(ErrorCode.USER_NOT_FOUND, "Assigned for user not found"));

            SystemUsers assignedBy = usersRepository.findById(requestDTO.getAssignedById())
                    .orElseThrow(() -> new ClientBackendException(ErrorCode.USER_NOT_FOUND, "Assigned by user not found"));

            SystemUsers createdBy = usersRepository.findById(requestDTO.getCreatedById())
                    .orElseThrow(() -> new ClientBackendException(ErrorCode.USER_NOT_FOUND, "Created by user not found"));

            Units unitCreatedBy = unitRepository.findById(createdBy.getUnitId())
                    .orElseThrow(() -> new ClientBackendException(ErrorCode.UNIT_NOT_FOUND));

            Problems problem = problemRepository.findById(requestDTO.getProblemTypeId())
                    .orElseThrow(() -> new ClientBackendException(ErrorCode.PROBLEM_NOT_FOUND));

            Map<String, Object> properties = new HashMap<>();
            properties.put("assignedBy", assignedBy.getName());
            properties.put("createdBy", createdBy.getName());
            properties.put("unit", unitCreatedBy.getUnitName());
            properties.put("dueDate", requestDTO.getDueDate());
            properties.put("typeProblem", problem.getProblem());
            properties.put("taskDescription", requestDTO.getDescription());
            properties.put("priority", requestDTO.getPriority());

            emailNotificationService.sendNotification(assignedForEmail, "Нове завдання!", properties, "new-assigned-task.ftl");

            requestDTO.setNotified(true);
        }

        if (requestDTO.getStatus() == Status.COMPLETED) {
            taskById.setCompletedDate(LocalDateTime.now());
        } else {
            taskById.setCompletedDate(null); // Clear the completedDate if the status is not COMPLETED
        }

        tasksMapper.patchMerge(requestDTO, taskById);
        return tasksMapper.mapToDTO(taskById);
    }
    @Override
    @Transactional
    public TaskResponseDTO patchByUser (UUID id, PatchByUserTaskRequestDTO requestDTO){

        var taskById = tasksRepository.findById(id)
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

    private Specification<Tasks> getSearchSpecification(TaskFilter taskFilter) {
        return Specification.where((Specification<Tasks>) searchLikeString("name", taskFilter.getSearch()))
                .and((Specification<Tasks>) searchFieldInCollectionOfIds("id", taskFilter.getIds()))
                .and((Specification<Tasks>) searchFieldInCollectionOfIds("assignedForId", taskFilter.getAssignedForIds()))
                .and((Specification<Tasks>) searchFieldInCollectionOfIds("assignedById", taskFilter.getAssignedByIds()))
                .and((Specification<Tasks>) searchFieldInCollectionOfIds("createdById", taskFilter.getCreatedByIds()))
                .and((Specification<Tasks>) searchOnStatus(taskFilter.getStatuses()))
                .and((Specification<Tasks>) searchOnPriority(taskFilter.getPriorities()))
                .and((Specification<Tasks>) searchByDueDate("dueDate", taskFilter.getDueDate()));
    }
}
