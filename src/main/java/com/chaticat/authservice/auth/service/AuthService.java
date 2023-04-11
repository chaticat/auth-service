package com.chaticat.authservice.auth.service;

import com.chaticat.authservice.auth.payload.request.LoginRequest;
import com.chaticat.authservice.auth.payload.request.SignUpRequest;
import com.chaticat.authservice.auth.payload.request.TokenRequest;
import com.chaticat.authservice.auth.payload.response.JwtAuthenticationResponse;
import com.chaticat.authservice.config.PropertiesConfig;
import com.chaticat.authservice.exception.UserNotFoundException;
import com.chaticat.authservice.persistence.entity.User;
import com.chaticat.authservice.persistence.repository.UserRepository;
import com.chaticat.authservice.security.JwtTokenProvider;
import com.chaticat.authservice.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final PropertiesConfig.JwtProperties properties;

    @Transactional(readOnly = true)
    public Boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Transactional
    public void registerUser(SignUpRequest signUpRequest) {
        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));

        userRepository.save(user);
    }

    @Transactional
    public JwtAuthenticationResponse loginUser(LoginRequest loginRequest) {
        var user = getValidUser(loginRequest);

        var userPrincipal = UserPrincipal.create(user);
        var authentication = new UsernamePasswordAuthenticationToken(userPrincipal, loginRequest.getPassword());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        var accessToken = createAccessToken(authentication);
        var refreshToken = createRefreshToken(authentication);
        var userId = tokenProvider.getUserIdFromToken(accessToken);

        return new JwtAuthenticationResponse(accessToken, refreshToken, userId);
    }

    private User getValidUser(LoginRequest loginRequest) {
        var user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + loginRequest.getUsername()));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new UserNotFoundException("Bad credentials");
        }

        return user;
    }

    public JwtAuthenticationResponse generateTokensFromUserEntity(User user) {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        null
                )
        );

        String accessToken = tokenProvider.generateToken(user.getId(), properties.getExpirationAccessToken());
        String refreshToken = tokenProvider.generateToken(user.getId(), properties.getExpirationRefreshToken());
        UUID userId = tokenProvider.getUserIdFromToken(accessToken);

        return new JwtAuthenticationResponse(accessToken, refreshToken, userId);
    }

    public JwtAuthenticationResponse generateNewTokens(TokenRequest tokenRequest) {
        UUID userId = tokenProvider.getUserIdFromToken(tokenRequest.getRefreshToken());
        String newAccessToken = tokenProvider.generateToken(userId, properties.getExpirationAccessToken());
        String newRefreshToken = tokenProvider.generateToken(userId, properties.getExpirationRefreshToken());

        return new JwtAuthenticationResponse(newAccessToken, newRefreshToken, userId);
    }

    public String createAccessToken(Authentication authentication) {
        return tokenProvider.generateToken(authentication, properties.getExpirationAccessToken());
    }

    public String createRefreshToken(Authentication authentication) {
        return tokenProvider.generateToken(authentication, properties.getExpirationRefreshToken());
    }

}
