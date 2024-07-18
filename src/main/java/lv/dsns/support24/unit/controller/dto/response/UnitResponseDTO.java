package lv.dsns.support24.unit.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lv.dsns.support24.unit.controller.dto.enums.UnitType;

import java.time.LocalDateTime;
import java.util.UUID;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UnitResponseDTO {
    private UUID id;
    private String unitName;
    private UnitType unitType;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
