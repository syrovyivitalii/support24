package lv.dsns.support24.notify.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotifyResponseDTO {

    @JsonProperty("uuid")
    private UUID eventId;

    @JsonProperty("ok")
    private boolean notificationStatus;

    @JsonProperty("desc")
    private String notificationDescription;
}
