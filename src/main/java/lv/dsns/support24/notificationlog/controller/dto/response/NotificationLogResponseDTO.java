package lv.dsns.support24.notificationlog.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lv.dsns.support24.nabatgroup.controller.dto.response.NabatGroupResponseDTO;
import lv.dsns.support24.user.controller.dto.response.UserResponseDTO;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationLogResponseDTO {
    private UUID id;
    private NabatGroupResponseDTO notificationLogGroup;
    private UUID notificationId;
    private String message;
    private String jsonResponse;
    private String jsonRequest;
    private UserResponseDTO notificationLogUser;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
