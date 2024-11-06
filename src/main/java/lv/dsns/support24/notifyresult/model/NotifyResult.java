package lv.dsns.support24.notifyresult.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotifyResult {
    @JsonProperty("id")
    private UUID id;

    @JsonProperty("volunteers")
    private List<NotifiedUser> notifiedUsers;

    @JsonProperty("description")
    private String description;

    @JsonProperty("dtmf_len")
    private String dtmfLen;

    @JsonProperty("eventType")
    private String eventType;
}
