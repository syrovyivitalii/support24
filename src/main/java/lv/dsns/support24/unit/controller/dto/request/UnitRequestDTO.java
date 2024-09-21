package lv.dsns.support24.unit.controller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lv.dsns.support24.unit.controller.dto.enums.UnitType;
import lv.dsns.support24.unit.repository.entity.Unit;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UnitRequestDTO {
    private String unitName;
    private UnitType unitType;
    private Unit parentUnit;
    private int groupId;

}
