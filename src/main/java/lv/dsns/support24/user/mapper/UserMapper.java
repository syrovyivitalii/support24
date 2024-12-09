package lv.dsns.support24.user.mapper;

import lombok.RequiredArgsConstructor;
import lv.dsns.support24.user.controller.dto.request.UserDefaultRequestDTO;
import lv.dsns.support24.user.controller.dto.request.UserRequestDTO;
import lv.dsns.support24.user.controller.dto.response.UserResponseDTO;
import lv.dsns.support24.user.repository.entity.SystemUsers;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Mapping(target = "password", source = "password", qualifiedByName = "encodePassword")
    public abstract SystemUsers mapToEntity (UserRequestDTO userRequestDTO);

    public abstract UserResponseDTO mapToDTO(SystemUsers systemUsers);

    @Mapping(target = "verify", constant = "false")
    @Mapping(target = "password", source = "defaultPassword", qualifiedByName = "generateDefaultPassword")
    public abstract SystemUsers mapToDefaultEntity (UserDefaultRequestDTO userDefaultRequestDTO, String defaultPassword);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract void patchMerge(UserRequestDTO userRequestDTO, @MappingTarget SystemUsers systemUsers);

    @Named("generateDefaultPassword")
    String generateDefaultPassword(String defaultPassword) {
        return passwordEncoder.encode(defaultPassword);
    }

    @Named("encodePassword")
    String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}
