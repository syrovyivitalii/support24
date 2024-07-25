package lv.dsns.support24.user.service;

import lv.dsns.support24.common.security.dto.AuthenticationRequest;
import lv.dsns.support24.common.security.dto.AuthenticationResponse;
import lv.dsns.support24.common.security.dto.RegisterRequest;

public interface AuthenticationService {
    AuthenticationResponse register (RegisterRequest request);
    AuthenticationResponse authenticate (AuthenticationRequest request);
    void changePassword(String email, String currentPassword, String newPassword);
    void changePasswordByAdmin(String email, String newPassword);
}
