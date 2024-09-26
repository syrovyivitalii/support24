package lv.dsns.support24.unit.mapper;


import lv.dsns.support24.task.controller.dto.enums.Status;
import lv.dsns.support24.unit.controller.dto.request.UnitRequestDTO;
import lv.dsns.support24.unit.controller.dto.response.UnitResponseDTO;
import lv.dsns.support24.unit.repository.entity.Unit;
import org.mapstruct.*;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring", imports = {LocalDateTime.class, Status.class})
public interface UnitMapper {
    Unit mapToEntity(UnitRequestDTO unitRequestDTO);
    UnitResponseDTO mapToDTO (Unit unit);

    @Mapping(target = "updatedDate",expression = "java(LocalDateTime.now())")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void patchMerge(UnitRequestDTO unitRequestDTO, @MappingTarget Unit unit);

}
