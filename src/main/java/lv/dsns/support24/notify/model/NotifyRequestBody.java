package lv.dsns.support24.notify.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotifyRequestBody {
    @JsonProperty("text")
    private String text;

    @JsonProperty("voice")
    private String voice;

    @JsonProperty("dtmf_len")
    private String dtmfLen;

    @JsonProperty("volunteers")
    private List<NotifyUser> notifyUsers;
}
