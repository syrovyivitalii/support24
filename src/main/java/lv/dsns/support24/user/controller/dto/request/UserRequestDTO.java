package lv.dsns.support24.user.controller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lv.dsns.support24.user.controller.dto.enums.Role;
import lv.dsns.support24.user.controller.dto.enums.Shift;
import lv.dsns.support24.user.controller.dto.enums.UserStatus;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {

    private String email;
    private Role role;
    private String password;
    private Shift shift;
    private UserStatus status;
    private String name;
    private UUID positionId;
    private boolean verify;
    private UUID unitId;
    private UUID rankId;
    private UUID permissionUnitId;
}
