package lv.dsns.support24.common.smtp;

import lv.dsns.support24.common.exception.ClientBackendException;
import lv.dsns.support24.common.exception.ErrorCode;
import lv.dsns.support24.problems.repository.ProblemRepository;
import lv.dsns.support24.problems.repository.entity.Problems;
import lv.dsns.support24.task.controller.dto.request.TaskRequestDTO;
import lv.dsns.support24.task.repository.entity.Task;
import lv.dsns.support24.unit.repository.UnitRepository;
import lv.dsns.support24.unit.repository.entity.Units;
import lv.dsns.support24.user.controller.dto.enums.Role;
import lv.dsns.support24.user.repository.SystemUsersRepository;
import lv.dsns.support24.user.repository.entity.SystemUsers;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Component
public class EmailNotificationFactory {
    private final EmailNotificationService emailNotificationService;
    private final SystemUsersRepository usersRepository;
    private final UnitRepository unitRepository;
    private final ProblemRepository problemRepository;

    public EmailNotificationFactory(EmailNotificationService emailNotificationService, SystemUsersRepository usersRepository,
                                    UnitRepository unitRepository, ProblemRepository problemRepository) {
        this.emailNotificationService = emailNotificationService;
        this.usersRepository = usersRepository;
        this.unitRepository = unitRepository;
        this.problemRepository = problemRepository;
    }

    public void sendTaskCreatedNotification(Task task) {
        SystemUsers userById = usersRepository.findById(task.getCreatedById())
                .orElseThrow(() -> new ClientBackendException(ErrorCode.USER_NOT_FOUND, "Assigned by user not found"));
        Units unitById = unitRepository.findById(userById.getUnitId())
                .orElseThrow(() -> new ClientBackendException(ErrorCode.UNIT_NOT_FOUND));
        Problems problemById = problemRepository.findById(task.getProblemTypeId())
                .orElseThrow(() -> new ClientBackendException(ErrorCode.PROBLEM_NOT_FOUND));

        List<String> optionalEmails = usersRepository.findEmailsByRole(Role.ROLE_SUPER_ADMIN);

        if (optionalEmails.isEmpty()) {
            optionalEmails.add("v.syrovyi@dsns.gov.ua");
        }

        Map<String, Object> properties = new HashMap<>();
        properties.put("userName", userById.getName());
        properties.put("union", unitById.getUnitName());
        properties.put("taskDescription", task.getDescription());
        properties.put("typeProblem", problemById.getProblem());

        for (String recipient : optionalEmails) {
            emailNotificationService.sendNotification(
                    recipient,
                    "Нове звернення з проблемою!",
                    properties,
                    "new-task-email.ftl"
            );
        }
    }
    public void sendTaskAssignedNotification(TaskRequestDTO requestDTO){
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
    }

}
