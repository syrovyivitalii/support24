package lv.dsns.support24.user.controller;

import lombok.RequiredArgsConstructor;
import lv.dsns.support24.common.exception.ClientBackendException;
import lv.dsns.support24.common.exception.ErrorCode;
import lv.dsns.support24.common.security.dto.*;
import lv.dsns.support24.user.controller.dto.enums.Role;
import lv.dsns.support24.user.repository.SystemUsersRepository;
import lv.dsns.support24.user.repository.entity.SystemUsers;
import lv.dsns.support24.user.service.impl.AuthenticationServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/public/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationServiceImpl authenticationService;
    private final SystemUsersRepository usersRepository;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ){
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ){
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        String requestRefreshToken = request.getRefreshToken();
        return ResponseEntity.ok(authenticationService.refreshToken(requestRefreshToken));
    }
    @PutMapping("/change-password")
    public ResponseEntity<Void> changePassword(
            @RequestBody ChangePasswordRequestDTO request
    ){
        String userEmail = getAuthenticatedUserEmail(); // Implement this method to retrieve the email of the currently authenticated user
        authenticationService.changePassword(userEmail, request.getCurrentPassword(), request.getNewPassword());
        return ResponseEntity.ok().build();
    }
    @PutMapping("/change-password/by-admin")
    public ResponseEntity<Void> changePasswordByAdmin(
            @RequestBody ChangePasswordRequestDTO request
    ){
        String userEmail = getAuthenticatedUserEmail(); // Implement this method to retrieve the email of the currently authenticated user
        Optional<SystemUsers> byEmail = usersRepository.findByEmail(userEmail);
        if (byEmail.get().getRole().equals(Role.ROLE_SYSTEM_ADMIN)){
            authenticationService.changePasswordByAdmin(userEmail, request.getNewPassword());
        }else {
            new ClientBackendException(ErrorCode.FORBIDDEN);
        }
        return ResponseEntity.ok().build();
    }
    private String getAuthenticatedUserEmail() {
        // Retrieve the email of the currently authenticated user
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

}
