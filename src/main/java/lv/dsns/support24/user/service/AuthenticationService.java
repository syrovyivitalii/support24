package lv.dsns.support24.user.service;

import lv.dsns.support24.common.security.AuthenticationRequest;
import lv.dsns.support24.common.security.AuthenticationResponse;
import lv.dsns.support24.common.security.RegisterRequest;

public interface AuthenticationService {
    AuthenticationResponse register (RegisterRequest request);
    AuthenticationResponse authenticate (AuthenticationRequest request);
}
