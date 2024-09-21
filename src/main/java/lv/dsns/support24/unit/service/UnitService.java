package lv.dsns.support24.unit.service;

import lv.dsns.support24.unit.controller.dto.request.UnitRequestDTO;
import lv.dsns.support24.unit.controller.dto.response.UnitResponseDTO;
import lv.dsns.support24.unit.service.filter.UnitFilter;

import java.util.List;
import java.util.UUID;

public interface UnitService {
    UnitResponseDTO save (UnitRequestDTO unitRequestDTO);

    List<UnitResponseDTO> findAll(UnitFilter unitFilter);
    List<UnitResponseDTO> findAllChildUnits(UUID id);
    UnitResponseDTO patchUnit(UUID id, UnitRequestDTO requestDTO);
    void delete(UUID id);
    boolean existUnitByUnitName(String unitName);
}
