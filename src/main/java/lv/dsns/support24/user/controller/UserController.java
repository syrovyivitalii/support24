package lv.dsns.support24.user.controller;

import lv.dsns.support24.common.dto.response.PageResponse;
import lv.dsns.support24.user.controller.dto.request.UserRequestDTO;
import lv.dsns.support24.user.controller.dto.response.UserResponseDTO;
import lv.dsns.support24.user.service.filter.UserFilter;
import lv.dsns.support24.user.service.impl.UserServiceImpl;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/private/users")
public class UserController {
    private final UserServiceImpl userService;
    private final PasswordEncoder passwordEncoder;
    public UserController(UserServiceImpl userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllSystemUsers(){
        var allSystemUsers = userService.findAll();
        return ResponseEntity.ok(allSystemUsers);
    }
    @GetMapping("/pageable")
    public ResponseEntity<PageResponse<UserResponseDTO>> getAllSystemUsersPageable(@ParameterObject UserFilter userFilter, @SortDefault(sort = "name", direction = Sort.Direction.ASC) @ParameterObject Pageable pageable){
        PageResponse<UserResponseDTO> responseDTOS = userService.findAllPageable(userFilter,pageable);
        return ResponseEntity.ok(responseDTOS);
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> save (@RequestBody UserRequestDTO userRequestDTO){
        if (userRequestDTO.getPassword() == null){
            String encoded = passwordEncoder.encode("123");
            userRequestDTO.setPassword(encoded);
        }
        var responseDto = userService.save(userRequestDTO);
        return ResponseEntity.ok(responseDto);
    }
}
