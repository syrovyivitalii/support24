package lv.dsns.support24.nabat.controller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NabatRequestDTO {
    private UUID nabatGroupId;
    private UUID userId;
}
