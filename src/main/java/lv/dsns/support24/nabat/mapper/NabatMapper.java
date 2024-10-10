package lv.dsns.support24.nabat.mapper;

import lv.dsns.support24.nabat.controller.dto.request.NabatRequestDTO;
import lv.dsns.support24.nabat.controller.dto.response.NabatResponseDTO;
import lv.dsns.support24.nabat.repository.entity.Nabat;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NabatMapper {
    Nabat mapToEntity(NabatRequestDTO nabatRequestDTO);
    NabatResponseDTO mapToDTO(Nabat nabat);
}
