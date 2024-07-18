package lv.dsns.support24.unit.service;

import lv.dsns.support24.unit.controller.dto.request.UnitRequestDTO;
import lv.dsns.support24.unit.controller.dto.response.UnitResponseDTO;

public interface UnitService {
    UnitResponseDTO save (UnitRequestDTO unitRequestDTO);

    boolean existUnitByUnitName(String unitName);
}
