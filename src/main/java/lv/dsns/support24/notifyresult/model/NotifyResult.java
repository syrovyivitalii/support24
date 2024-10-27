package lv.dsns.support24.notifyresult.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lv.dsns.support24.notify.model.NotifyUser;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotifyResult {
    private UUID id;

    private String description;

    @JsonProperty("volunteers")
    private List<NotifyUser> notifiedUsers;

    @JsonProperty("dtmf_len")
    private String dtmfLen;}
