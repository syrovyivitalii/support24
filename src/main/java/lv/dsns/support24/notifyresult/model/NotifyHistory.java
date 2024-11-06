package lv.dsns.support24.notifyresult.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotifyHistory {
    @JsonProperty("reason")
    private String reason;

    @JsonProperty("label")
    private String label;

    @JsonProperty("start_at")
    private LocalDateTime startedAt;
}
