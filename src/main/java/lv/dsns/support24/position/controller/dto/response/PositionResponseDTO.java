package lv.dsns.support24.position.controller.dto.response;

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
public class PositionResponseDTO {
    private UUID id;
    private String positionName;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
