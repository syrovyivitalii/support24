package lv.dsns.support24.user.service;

import lv.dsns.support24.common.dto.response.PageResponse;
import lv.dsns.support24.user.controller.dto.request.UserDefaultRequestDTO;
import lv.dsns.support24.user.controller.dto.request.UserRequestDTO;
import lv.dsns.support24.user.controller.dto.response.UserResponseDTO;
import lv.dsns.support24.user.repository.entity.SystemUsers;
import lv.dsns.support24.user.service.filter.UserFilter;
import org.springframework.data.domain.Pageable;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

public interface UserService {
    List<UserResponseDTO> findAll(UserFilter userFilter);

    PageResponse<UserResponseDTO> findAllPageable(UserFilter userFilter, Pageable pageable);

    List<UserResponseDTO> findAllSubordinated(Principal principal, UserFilter userFilter);

    UserResponseDTO save(UserRequestDTO userRequestDTO);

    UserResponseDTO saveDefault(UserDefaultRequestDTO userDefaultRequestDTO);

    boolean existUserByEmail(String email);

    UserResponseDTO patch (UUID id, UserRequestDTO requestDTO);

    void delete(UUID id);

    PageResponse<UserResponseDTO> findAllSubordinatedPageable(Principal principal, UserFilter userFilter, Pageable pageable);

    SystemUsers getUserByEmail(String email);
}
