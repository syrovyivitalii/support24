package lv.dsns.support24.unit.mapper;

import lv.dsns.support24.unit.controller.request.UnitRequestDTO;
import lv.dsns.support24.unit.controller.response.UnitResponseDTO;
import lv.dsns.support24.unit.repository.entity.Units;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UnitMapper {
    Units mapToEntity(UnitRequestDTO unitRequestDTO);

    UnitResponseDTO mapToDTO (Units units);
}
