package lv.dsns.support24.position.mapper;

import lv.dsns.support24.position.controller.dto.request.PositionRequestDTO;
import lv.dsns.support24.position.controller.dto.response.PositionResponseDTO;
import lv.dsns.support24.position.repository.entity.Position;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PositionMapper {

    Position mapToEntity(PositionRequestDTO positionRequestDTO);

    PositionResponseDTO mapToDTO(Position position);
}
