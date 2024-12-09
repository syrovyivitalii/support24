package lv.dsns.support24.auth.service;

import lv.dsns.support24.auth.controller.dto.*;

import java.security.Principal;

public interface AuthService {
    AuthenticationResponse register (RegisterRequest request);

    AuthenticationResponse authenticate (AuthenticationRequest request);

    AuthenticationResponse refreshToken(String refreshToken);

    void changePassword(Principal principal, ChangePasswordRequestDTO request);

    void changePasswordByAdmin(Principal principal, ChangePasswordByAdminRequestDTO request);
}
