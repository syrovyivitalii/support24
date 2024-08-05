package lv.dsns.support24.user.controller;

import lv.dsns.support24.common.dto.response.PageResponse;
import lv.dsns.support24.task.controller.dto.request.TaskRequestDTO;
import lv.dsns.support24.task.controller.dto.response.TaskResponseDTO;
import lv.dsns.support24.user.controller.dto.request.UserRequestDTO;
import lv.dsns.support24.user.controller.dto.response.UserResponseDTO;
import lv.dsns.support24.user.service.UserService;
import lv.dsns.support24.user.service.filter.UserFilter;
import lv.dsns.support24.user.service.impl.UserServiceImpl;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    public UserController(UserServiceImpl userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/private/users")
    public ResponseEntity<List<UserResponseDTO>> getAllSystemUsers(@ParameterObject UserFilter userFilter){
        var allSystemUsers = userService.findAll(userFilter);
        return ResponseEntity.ok(allSystemUsers);
    }
    @GetMapping("/public/users/pageable")
    public ResponseEntity<PageResponse<UserResponseDTO>> getAllSystemUsersPageable(@ParameterObject UserFilter userFilter, @SortDefault(sort = "name", direction = Sort.Direction.ASC) @ParameterObject Pageable pageable){
        PageResponse<UserResponseDTO> responseDTOS = userService.findAllPageable(userFilter,pageable);
        return ResponseEntity.ok(responseDTOS);
    }

    @PostMapping("/private/users")
    public ResponseEntity<UserResponseDTO> save (@RequestBody UserRequestDTO userRequestDTO){
        var responseDto = userService.save(userRequestDTO);
        return ResponseEntity.ok(responseDto);
    }
    @PostMapping("/public/users/default")
    public ResponseEntity<UserResponseDTO> saveDefault (@RequestBody UserRequestDTO userRequestDTO){
        var responseDto = userService.saveDefault(userRequestDTO);
        return ResponseEntity.ok(responseDto);
    }
    @PatchMapping("/public/users/{id}")
    public ResponseEntity<UserResponseDTO> patch(@PathVariable UUID id, @RequestBody UserRequestDTO requestDTO){
        var patchedTask = userService.patch(id,requestDTO);

        return ResponseEntity.ok(patchedTask);
    }
    @DeleteMapping("/private/users/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id){
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
