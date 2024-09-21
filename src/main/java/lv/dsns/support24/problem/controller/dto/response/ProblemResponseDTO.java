package lv.dsns.support24.problem.controller.dto.response;

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
public class ProblemResponseDTO {
    private UUID id;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private String problem;
}
