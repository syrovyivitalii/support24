package lv.dsns.support24.unit.controller.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lv.dsns.support24.unit.controller.enums.UnitType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UnitRequestDTO {
    private String unitName;
    private UnitType unitType;
}
