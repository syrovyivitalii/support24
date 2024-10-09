package lv.dsns.support24.rank.controller.dto.response;

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
public class RankResponseDTO {
    private UUID id;
    private String rankName;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
