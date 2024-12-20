package lv.dsns.support24.task.controller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lv.dsns.support24.task.controller.dto.enums.Priority;
import lv.dsns.support24.task.controller.dto.enums.Status;
import lv.dsns.support24.task.controller.dto.enums.Type;


import java.time.LocalDateTime;
import java.util.UUID;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskRequestDTO {

    private String description;
    private String comment;
    private LocalDateTime dueDate;
    private LocalDateTime completedDate;
    private Status status;
    private Priority priority;
    private Type taskType;
    private UUID assignedForId;
    private UUID assignedById;
    private UUID createdById;
    private UUID problemTypeId;
    private UUID parentId;
}
