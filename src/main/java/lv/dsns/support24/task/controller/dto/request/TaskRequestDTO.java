package lv.dsns.support24.task.controller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lv.dsns.support24.task.controller.dto.enums.Priority;
import lv.dsns.support24.task.controller.dto.enums.Status;
import lv.dsns.support24.user.controller.dto.request.UserRequestDTO;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskRequestDTO {
    private String name;
    private String description;
    private Status status;
    private Priority priority;
    private UserRequestDTO createdForId;
    private UserRequestDTO createdById;
}
