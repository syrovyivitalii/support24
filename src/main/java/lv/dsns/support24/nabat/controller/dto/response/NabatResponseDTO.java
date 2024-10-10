package lv.dsns.support24.nabat.controller.dto.response;

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
public class NabatResponseDTO {
    private UUID id;
    private UUID nabatGroupId;
    private UUID userId;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
}
