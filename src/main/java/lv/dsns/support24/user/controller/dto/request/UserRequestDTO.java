package lv.dsns.support24.user.controller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lv.dsns.support24.user.controller.dto.enums.Role;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {
    private String email;
    private Role role;
    private String password;
    private boolean verify;
}
