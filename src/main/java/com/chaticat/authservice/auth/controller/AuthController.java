package com.chaticat.authservice.auth.controller;

import com.chaticat.authservice.auth.payload.request.LoginRequest;
import com.chaticat.authservice.auth.payload.request.SignUpRequest;
import com.chaticat.authservice.auth.payload.request.TokenRequest;
import com.chaticat.authservice.auth.payload.response.ApiResponse;
import com.chaticat.authservice.auth.payload.response.JwtAuthenticationResponse;
import com.chaticat.authservice.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@Validated
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sign-up")
    public ResponseEntity<ApiResponse> registerUser(@RequestBody SignUpRequest signUpRequest) {
        ApiResponse apiResponse;

        if (authService.existsByUsername(signUpRequest.getUsername())) {
            apiResponse = new ApiResponse(false, "Username  is already taken!");
        } else {
            apiResponse = new ApiResponse(true, "User registered successfully");
            authService.registerUser(signUpRequest);
        }

        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<JwtAuthenticationResponse> authenticateUser(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.loginUser(loginRequest));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<Object> refreshJwtToken(@RequestBody TokenRequest tokenRequest) {
        return ResponseEntity.ok(authService.refreshAccessToken(tokenRequest));
    }

    @GetMapping("/username-exists")
    public ResponseEntity<Boolean> checkIfUsernameExists(@RequestParam String username) {
        return ResponseEntity.ok(authService.existsByUsername(username));
    }
}

