package lv.dsns.support24.user.controller;

import lv.dsns.support24.user.controller.dto.request.UserRequestDTO;
import lv.dsns.support24.user.controller.dto.response.UserResponseDTO;
import lv.dsns.support24.user.service.impl.UserServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {
    private final UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping
//    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<List<UserResponseDTO>> getAllSystemUsers(){
        var allSystemUsers = userService.findAll();
        return ResponseEntity.ok(allSystemUsers);
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> save (@RequestBody UserRequestDTO userRequestDTO){
        var responseDto = userService.save(userRequestDTO);
        return ResponseEntity.ok(responseDto);
    }
}
