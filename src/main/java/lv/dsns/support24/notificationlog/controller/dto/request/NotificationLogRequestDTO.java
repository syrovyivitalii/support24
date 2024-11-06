package lv.dsns.support24.notificationlog.controller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationLogRequestDTO {
    private String message;
    private String jsonResponse;
    private UUID nabatGroupId;
    private UUID eventId;
    private UUID notifiedByUserId;
}
