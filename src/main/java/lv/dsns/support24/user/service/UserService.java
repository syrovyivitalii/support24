package lv.dsns.support24.user.service;

import lv.dsns.support24.common.dto.response.PageResponse;
import lv.dsns.support24.user.controller.dto.request.UserRequestDTO;
import lv.dsns.support24.user.controller.dto.response.UserResponseDTO;
import lv.dsns.support24.user.service.filter.UserFilter;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface UserService {
    List<UserResponseDTO> findAll();
    PageResponse<UserResponseDTO> findAllPageable(UserFilter userFilter, Pageable pageable);
    UserResponseDTO save(UserRequestDTO userRequestDTO);
    UserResponseDTO saveDefault(UserRequestDTO userRequestDTO);
    boolean existUserByEmail(String email);
    UserResponseDTO patch (UUID id, UserRequestDTO requestDTO);
    void delete(UUID id);
}
