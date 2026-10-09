package com.searchfeed.userservice.controller;

import com.searchfeed.userservice.dto.AuthResponse;
import com.searchfeed.userservice.dto.LoginRequest;
import com.searchfeed.userservice.dto.RegisterRequest;
import com.searchfeed.userservice.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@Slf4j
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    public ResponseEntity<AuthResponse> register(
            @Valid @RequestBody RegisterRequest registerRequest
    ){
        log.info("Registering Request for  :{}", registerRequest.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authService.register(registerRequest));
    }

    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest loginRequest
    ){
        log.info("Logging Request For  :{}", loginRequest.getEmail());
        return ResponseEntity.ok(authService.login(loginRequest));
    }

}
