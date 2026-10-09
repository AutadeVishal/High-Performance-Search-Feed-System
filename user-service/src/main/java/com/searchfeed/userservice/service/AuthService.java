package com.searchfeed.userservice.service;

import com.searchfeed.userservice.dto.AuthResponse;
import com.searchfeed.userservice.dto.LoginRequest;
import com.searchfeed.userservice.dto.RegisterRequest;
import com.searchfeed.userservice.respository.UserRespository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {
    private final UserRespository userRespository;
    public AuthResponse register(RegisterRequest request) {
        log.info("Registering User : {}",request.getEmail());
        if(userRespository.existsByEmail(request.getEmail()){
            throw new UserRegistrationException("Email Already Exists");
        }
    }

}
