package lv.dsns.support24.unit.service.filter;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import lv.dsns.support24.common.util.filter.SearchFilter;
import lv.dsns.support24.task.controller.dto.enums.Status;
import lv.dsns.support24.unit.controller.dto.enums.UnitStatus;
import lv.dsns.support24.unit.controller.dto.enums.UnitType;

import java.util.Set;

import static lombok.AccessLevel.PRIVATE;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@FieldDefaults(level = PRIVATE)
public class UnitFilter extends SearchFilter {
    Set<UnitType> unitType;
    String unitName;
    Set<UnitStatus> statuses;
}
