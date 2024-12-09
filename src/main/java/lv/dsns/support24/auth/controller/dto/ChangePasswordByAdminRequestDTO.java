package lv.dsns.support24.auth.controller.dto;

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
