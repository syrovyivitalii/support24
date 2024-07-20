package lv.dsns.support24.unit.service;

import lv.dsns.support24.unit.controller.dto.request.UnitRequestDTO;
import lv.dsns.support24.unit.controller.dto.response.UnitResponseDTO;
import lv.dsns.support24.unit.service.filter.UnitFilter;

import java.util.List;

public interface UnitService {
    UnitResponseDTO save (UnitRequestDTO unitRequestDTO);

    List<UnitResponseDTO> findAll(UnitFilter unitFilter);

    boolean existUnitByUnitName(String unitName);
}
