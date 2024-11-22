package lv.dsns.support24.notifyresult.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotifyResultResponseDTO {
    private List<NotifyResultInfo> notifiedUsers;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class NotifyResultInfo {
        private int soduId;
        private String mobilePhone;
        private boolean notifyStatus;
    }
}
