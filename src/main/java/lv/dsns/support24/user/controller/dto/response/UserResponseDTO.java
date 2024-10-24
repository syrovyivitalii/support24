package lv.dsns.support24.user.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lv.dsns.support24.position.controller.dto.response.PositionResponseDTO;
import lv.dsns.support24.rank.controller.dto.response.RankResponseDTO;
import lv.dsns.support24.unit.controller.dto.response.UnitResponseDTO;
import lv.dsns.support24.user.controller.dto.enums.Role;
import lv.dsns.support24.user.controller.dto.enums.Shift;
import lv.dsns.support24.user.controller.dto.enums.UserStatus;

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
    private Shift shift;
    private UserStatus status;
    private boolean verify;
    private String name;
    private PositionResponseDTO userPosition;
    private RankResponseDTO userRank;
    private UnitResponseDTO userUnit;
}
