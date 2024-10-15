package lv.dsns.support24.nabat.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lv.dsns.support24.user.controller.dto.response.UserResponseDTO;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NabatResponseDTO {
    private UUID id;
    private UUID nabatGroupId;
    private UserResponseDTO nabatUsers;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
