package lv.dsns.support24.user.service.impl;

import lombok.RequiredArgsConstructor;
import lv.dsns.support24.common.exception.ClientBackendException;
import lv.dsns.support24.common.exception.ErrorCode;
import lv.dsns.support24.common.security.*;
import lv.dsns.support24.common.security.dto.*;
import lv.dsns.support24.user.controller.dto.enums.Role;
import lv.dsns.support24.user.controller.dto.enums.UserStatus;
import lv.dsns.support24.user.repository.SystemUsersRepository;
import lv.dsns.support24.user.repository.entity.SystemUsers;
import lv.dsns.support24.user.service.AuthenticationService;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final SystemUsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthenticationResponse register(RegisterRequest request) {
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

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var user = usersRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ClientBackendException(ErrorCode.USER_NOT_FOUND));

        if (!UserStatus.ACTIVE.equals(user.getStatus())) {
            throw new ClientBackendException(ErrorCode.USER_NOT_ACTIVE);
        }

        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        }catch(AuthenticationException e){
            throw new ClientBackendException(ErrorCode.INVALID_CREDENTIALS);
        }

        var accessToken = jwtService.generateToken((UserDetails) user);
        var refreshToken = jwtService.generateRefreshToken((UserDetails) user);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public AuthenticationResponse refreshToken(String refreshToken) {
        if (jwtService.isTokenExpired(refreshToken)) {
            throw new ClientBackendException(ErrorCode.REFRESH_TOKEN_EXPIRED);
        }

        String userEmail = jwtService.extractEmail(refreshToken);
        var user = usersRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ClientBackendException(ErrorCode.USER_NOT_FOUND));

        if (!user.getStatus().equals(UserStatus.ACTIVE)) {
            throw new ClientBackendException(ErrorCode.USER_NOT_ACTIVE);
        }

        var accessToken = jwtService.generateToken((UserDetails) user);
        var newRefreshToken = jwtService.generateRefreshToken((UserDetails) user);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

    @Override
    public void changePassword(Principal principal, ChangePasswordRequestDTO request) {
        var user = usersRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new ClientBackendException(ErrorCode.USER_NOT_FOUND));

        if (!UserStatus.ACTIVE.equals(user.getStatus())) {
            throw new ClientBackendException(ErrorCode.USER_NOT_ACTIVE);
        }

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new ClientBackendException(ErrorCode.INVALID_CURRENT_PASSWORD);
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        usersRepository.save(user);
    }

    @Override
    public void changePasswordByAdmin(Principal principal, ChangePasswordByAdminRequestDTO request) {

        Optional<SystemUsers> byEmail = usersRepository.findByEmail(principal.getName());
        byEmail.ifPresent(systemUsers -> {
            if (byEmail.get().getRole().equals(Role.ROLE_SYSTEM_ADMIN)){
                var user = usersRepository.findByEmail(request.getEmail())
                        .orElseThrow(() -> new ClientBackendException(ErrorCode.USER_NOT_FOUND));

                if (!user.getStatus().equals(UserStatus.ACTIVE)) {
                    throw new ClientBackendException(ErrorCode.USER_NOT_ACTIVE);
                }

                user.setPassword(passwordEncoder.encode(request.getNewPassword()));
                usersRepository.save(user);
            }
        });
    }
}
