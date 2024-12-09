package lv.dsns.support24.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lv.dsns.support24.auth.controller.dto.*;
import lv.dsns.support24.auth.service.AuthService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/public/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authenticationService;

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
            @RequestBody ChangePasswordRequestDTO request,
            @ParameterObject Principal principal
    ){

        authenticationService.changePassword(principal, request);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/change-password/by-admin")
    @PreAuthorize("hasRole('ROLE_SYSTEM_ADMIN')")
    @Operation(summary = "Accessible by ROLE_SYSTEM_ADMIN only")
    public ResponseEntity<Void> changePasswordByAdmin(
            @RequestBody ChangePasswordByAdminRequestDTO request,
            @ParameterObject Principal principal
    ){
        authenticationService.changePasswordByAdmin(principal, request);

        return ResponseEntity.ok().build();
    }
}
