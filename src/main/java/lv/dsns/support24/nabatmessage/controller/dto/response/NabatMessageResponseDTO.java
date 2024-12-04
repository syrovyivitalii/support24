package lv.dsns.support24.nabatmessage.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NabatMessageResponseDTO {
    private UUID id;
    private String nabatMessage;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
