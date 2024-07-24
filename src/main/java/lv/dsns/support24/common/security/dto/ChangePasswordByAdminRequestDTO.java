package lv.dsns.support24.common.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordByAdminRequestDTO {
    private String email;
    private String newPassword;
}
