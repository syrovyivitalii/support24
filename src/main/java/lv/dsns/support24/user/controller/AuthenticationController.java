package lv.dsns.support24.user.controller;

import lombok.RequiredArgsConstructor;
import lv.dsns.support24.common.security.dto.*;
import lv.dsns.support24.user.service.AuthenticationService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/public/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

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
    public ResponseEntity<Void> changePasswordByAdmin(
            @RequestBody ChangePasswordByAdminRequestDTO request,
            @ParameterObject Principal principal
    ){
        authenticationService.changePasswordByAdmin(principal, request);

        return ResponseEntity.ok().build();
    }
}
