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
public class NotifiedUser {
    @JsonProperty("id")
    private int id;

    @JsonProperty("mobilePhone")
    private String mobilePhone;

    @JsonProperty("event_id")
    private UUID eventId;

    @JsonProperty("dtmf")
    private String dtmf;

    private String status;

    @JsonProperty("notifyHistory")
    private List<NotifyHistory> notifyHistory;
}
