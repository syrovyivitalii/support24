package lv.dsns.support24.task.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lv.dsns.support24.problems.controller.dto.response.ProblemResponseDTO;
import lv.dsns.support24.task.controller.dto.enums.Priority;
import lv.dsns.support24.task.controller.dto.enums.Status;
import lv.dsns.support24.task.controller.dto.enums.Type;
import lv.dsns.support24.user.controller.dto.response.UserResponseDTO;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponseDTO {
    private UUID id;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private LocalDateTime completedDate;
    private String description;
    private LocalDateTime dueDate;
    private Status status;
    private Priority priority;
    private Type taskType;
//    private UUID assignedForId;
//    private UUID assignedById;
//    private UUID createdById;
    private ProblemResponseDTO taskProblem;
    private UUID parentId;
    private boolean notified;
    private UserResponseDTO createdBy;
    private UserResponseDTO assignedFor;
    private UserResponseDTO assignedBy;

}
