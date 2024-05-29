package lv.dsns.support24.task.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lv.dsns.support24.task.controller.dto.enums.Priority;
import lv.dsns.support24.task.controller.dto.enums.Status;
import lv.dsns.support24.user.controller.dto.request.UserRequestDTO;

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
    private String name;
    private String description;
    private LocalDateTime dueDate;
    private Status status;
    private Priority priority;
    private UserRequestDTO createdForId;
    private UserRequestDTO createdById;
}