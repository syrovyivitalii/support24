package lv.dsns.support24.nabatgroup.controller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NabatGroupRequestDTO {
    private UUID unitId;
    private String groupName;
}
