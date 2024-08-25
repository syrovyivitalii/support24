package lv.dsns.support24.task.controller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lv.dsns.support24.task.controller.dto.enums.Status;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatchByUserTaskRequestDTO {
    private String description;
    private Status status;
    private String comment;
}
