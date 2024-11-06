package lv.dsns.support24.phone.mapper;

import lv.dsns.support24.phone.controller.dto.request.PhoneRequestDTO;
import lv.dsns.support24.phone.controller.dto.response.PhoneResponseDTO;
import lv.dsns.support24.phone.repository.entity.Phone;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PhoneMapper {

    PhoneResponseDTO mapToDTO(Phone phone);

    Phone mapToEntity(PhoneRequestDTO phoneRequestDTO);
}
