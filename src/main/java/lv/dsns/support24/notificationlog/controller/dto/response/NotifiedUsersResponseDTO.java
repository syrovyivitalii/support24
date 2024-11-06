package lv.dsns.support24.notificationlog.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotifiedUsersResponseDTO {
    private List<NotifiedUserInfo> notifiedUsers;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class NotifiedUserInfo {
        private int soduId;
        private String name;
        private String mobilePhone;
        private boolean notifyStatus;
    }
}
