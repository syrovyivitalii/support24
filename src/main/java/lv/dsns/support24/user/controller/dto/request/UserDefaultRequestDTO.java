package lv.dsns.support24.user.controller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDefaultRequestDTO {
    private String email;
    private String password;
    private String name;
    private UUID unitId;
}
