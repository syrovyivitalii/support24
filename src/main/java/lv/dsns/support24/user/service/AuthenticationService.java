package lv.dsns.support24.user.service;

import lv.dsns.support24.common.security.dto.*;

import java.security.Principal;

public interface AuthenticationService {
    AuthenticationResponse register (RegisterRequest request);

    AuthenticationResponse authenticate (AuthenticationRequest request);

    AuthenticationResponse refreshToken(String refreshToken);

    void changePassword(Principal principal, ChangePasswordRequestDTO request);

    void changePasswordByAdmin(Principal principal, ChangePasswordByAdminRequestDTO request);
}
