package lv.dsns.support24.nabatgroup.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NabatGroupResponseDTO {
    private UUID id;
    private UUID unitId;
    private String groupName;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
