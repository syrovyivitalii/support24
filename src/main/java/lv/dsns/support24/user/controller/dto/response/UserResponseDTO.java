package lv.dsns.support24.user.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lv.dsns.support24.unit.controller.dto.response.UnitResponseDTO;
import lv.dsns.support24.user.controller.dto.enums.Role;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    private UUID id;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private String email;
    private Role role;
    private boolean verify;
    private String name;
    private String jobTitle;
    private UnitResponseDTO userUnit;

}
