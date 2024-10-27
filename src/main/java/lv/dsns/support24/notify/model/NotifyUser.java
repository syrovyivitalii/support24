package lv.dsns.support24.notify.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotifyUser {
    @JsonProperty("id")
    private int id;

    @JsonProperty("mobilePhone")
    private String mobilePhone;
}
