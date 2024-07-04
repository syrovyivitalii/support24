package lv.dsns.support24.user.service;

import lv.dsns.support24.user.controller.dto.request.UserRequestDTO;
import lv.dsns.support24.user.controller.dto.response.UserResponseDTO;

import java.util.List;

public interface UserService {
    List<UserResponseDTO> findAll();
    UserResponseDTO save(UserRequestDTO userRequestDTO);

    boolean existUserByEmail(String email);
}
