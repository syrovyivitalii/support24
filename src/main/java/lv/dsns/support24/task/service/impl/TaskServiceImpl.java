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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;
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
    public PageResponse<TaskResponseDTO> findAllPageable(TaskFilter taskFilter, Pageable pageable, UserDetails userDetails) {
        // Extract the role of the user
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        boolean isAdminOrSuperAdmin = authorities.stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN") || auth.getAuthority().equals("ROLE_SUPER_ADMIN"));

        Page<Tasks> allTasks;

        if (isAdminOrSuperAdmin) {
            // Admin or Super Admin: Get all tasks
            allTasks = tasksRepository.findAll(getSearchSpecification(taskFilter), pageable);
        } else {
            // Regular User: Get tasks assigned to them
            UUID userId = getUserIdFromUserDetails(userDetails);  // Method to extract the user's UUID from UserDetails
            taskFilter.setCreatedForIds(Set.of(userId));
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

    // Helper method to extract user UUID from UserDetails
    private UUID getUserIdFromUserDetails(UserDetails userDetails) {
        SystemUsers user = usersRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return user.getId();
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
