package lv.dsns.support24.task.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lv.dsns.support24.task.controller.dto.enums.Priority;
import lv.dsns.support24.task.controller.dto.enums.Status;
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
    private UUID assignedForId;
    private UUID assignedById;
    private UUID createdById;
    private UUID problemTypeId;
    private boolean notified;
    private UserResponseDTO assignedFor;

}
