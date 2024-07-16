package lv.dsns.support24.user.service.impl;

import lombok.RequiredArgsConstructor;
import lv.dsns.support24.common.exception.ClientBackendException;
import lv.dsns.support24.common.exception.ErrorCode;
import lv.dsns.support24.common.security.*;
import lv.dsns.support24.user.controller.dto.enums.Role;
import lv.dsns.support24.user.repository.SystemUsersRepository;
import lv.dsns.support24.user.repository.entity.SystemUsers;
import lv.dsns.support24.user.service.AuthenticationService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

   private final SystemUsersRepository usersRepository;
   private final PasswordEncoder passwordEncoder;

   private final JwtService jwtService;

   private final AuthenticationManager authenticationManager;
    public AuthenticationResponse register (RegisterRequest request){

        if (usersRepository.existsByEmail(request.getEmail())) {
            throw new ClientBackendException(ErrorCode.USER_ALREADY_EXISTS);
        }

        var user = SystemUsers.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_USER)
                .build();
        usersRepository.save(user);

        var accessToken = jwtService.generateToken((UserDetails) user);
        var refreshToken = jwtService.generateRefreshToken((UserDetails) user);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

    }
    public AuthenticationResponse authenticate (AuthenticationRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = usersRepository.findByEmail(request.getEmail())
                .orElseThrow();
        var accessToken = jwtService.generateToken((UserDetails) user);
        var refreshToken = jwtService.generateRefreshToken((UserDetails) user);
        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
    public AuthenticationResponse refreshToken(String refreshToken) {
        if (jwtService.isTokenExpired(refreshToken)) {
            throw new ClientBackendException(ErrorCode.REFRESH_TOKEN_EXPIRED);
        }

        String userEmail = jwtService.extractEmail(refreshToken);
        var user = usersRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ClientBackendException(ErrorCode.USER_NOT_FOUND));

        var accessToken = jwtService.generateToken((UserDetails) user);
        var newRefreshToken = jwtService.generateRefreshToken((UserDetails) user);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

}
