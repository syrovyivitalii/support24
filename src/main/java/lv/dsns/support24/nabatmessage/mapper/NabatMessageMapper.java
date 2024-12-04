package lv.dsns.support24.nabatmessage.mapper;

import lv.dsns.support24.nabatmessage.controller.dto.request.NabatMessageRequestDTO;
import lv.dsns.support24.nabatmessage.controller.dto.response.NabatMessageResponseDTO;
import lv.dsns.support24.nabatmessage.repository.entity.NabatMessage;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NabatMessageMapper {

    NabatMessage mapToEntity(NabatMessageRequestDTO nabatMessageRequestDTO);

    NabatMessageResponseDTO mapToDTO(NabatMessage nabatMessage);
}
