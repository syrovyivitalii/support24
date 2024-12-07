package lv.dsns.support24.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lv.dsns.support24.common.dto.response.PageResponse;
import lv.dsns.support24.user.controller.dto.request.UserDefaultRequestDTO;
import lv.dsns.support24.user.controller.dto.request.UserRequestDTO;
import lv.dsns.support24.user.controller.dto.response.UserResponseDTO;
import lv.dsns.support24.user.service.UserService;
import lv.dsns.support24.user.service.filter.UserFilter;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/private/users")
    public ResponseEntity<List<UserResponseDTO>> getAllSystemUsers(@ParameterObject UserFilter userFilter){
        var allSystemUsers = userService.findAll(userFilter);

        return ResponseEntity.ok(allSystemUsers);
    }

    @GetMapping("/private/users/pageable")
    public ResponseEntity<PageResponse<UserResponseDTO>> getAllSystemUsersPageable(@ParameterObject UserFilter userFilter, @SortDefault(sort = "name", direction = Sort.Direction.ASC) @ParameterObject Pageable pageable){
        PageResponse<UserResponseDTO> responseDTOS = userService.findAllPageable(userFilter,pageable);

        return ResponseEntity.ok(responseDTOS);
    }

    @GetMapping("/private/users/subordinated/pageable")
    @Operation(summary = "Get only subordinated users pageable (filter by child units)")
    public ResponseEntity<PageResponse<UserResponseDTO>> getAllSubordinatedUsersPageable(@ParameterObject Principal principal,
                                                                                 @ParameterObject UserFilter userFilter,
                                                                                 @SortDefault(sort = "name") @ParameterObject Pageable pageable){
        PageResponse<UserResponseDTO> responseDTOs = userService.findAllSubordinatedPageable(principal,userFilter,pageable);

        return ResponseEntity.ok(responseDTOs);
    }

    @GetMapping("/private/users/subordinated")
    @Operation(summary = "Get only subordinated users (filter by child units)")
    public ResponseEntity<List<UserResponseDTO>> getAllSubordinatedUsers(@ParameterObject Principal principal,
                                                                                 @ParameterObject UserFilter userFilter){
        List<UserResponseDTO> responseDTOs = userService.findAllSubordinated(principal,userFilter);

        return ResponseEntity.ok(responseDTOs);
    }


    @PostMapping("/private/users")
    public ResponseEntity<UserResponseDTO> save (@RequestBody UserRequestDTO userRequestDTO){
        var responseDto = userService.save(userRequestDTO);

        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/private/users/default")
    public ResponseEntity<UserResponseDTO> saveDefault (@RequestBody UserDefaultRequestDTO userDefaultRequestDTO){
        var responseDto = userService.saveDefault(userDefaultRequestDTO);

        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/private/users/{id}")
    public ResponseEntity<UserResponseDTO> patch(@PathVariable UUID id, @RequestBody UserRequestDTO requestDTO){
        var patchedTask = userService.patch(id,requestDTO);

        return ResponseEntity.ok(patchedTask);
    }

    @DeleteMapping("/private/users/delete/{id}")
    @PreAuthorize("hasRole('ROLE_SYSTEM_ADMIN')")
    @Operation(summary = "Accessible by ROLE_SYSTEM_ADMIN")
    public ResponseEntity<Void> delete(@PathVariable UUID id){
        userService.delete(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
