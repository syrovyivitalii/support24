package lv.dsns.support24.user.mapper;

import lv.dsns.support24.user.controller.dto.request.UserRequestDTO;
import lv.dsns.support24.user.controller.dto.response.UserResponseDTO;
import lv.dsns.support24.user.repository.entity.SystemUsers;
import org.mapstruct.*;
@Mapper(componentModel = "spring")
public interface UserMapper {

    SystemUsers mapToEntity (UserRequestDTO userRequestDTO);

    UserResponseDTO mapToDTO (SystemUsers systemUsers);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void patchMerge(UserRequestDTO userRequestDTO, @MappingTarget SystemUsers systemUsers);

}
