package com.searchfeed.userservice.service;

import com.searchfeed.userservice.dto.AuthResponse;
import com.searchfeed.userservice.dto.LoginRequest;
import com.searchfeed.userservice.dto.RegisterRequest;
import com.searchfeed.userservice.entity.User;
import com.searchfeed.userservice.entity.UserRole;
import com.searchfeed.userservice.event.UserCreatedEvent;
import com.searchfeed.userservice.exception.AuthenticationException;
import com.searchfeed.userservice.exception.UserNotFoundException;
import com.searchfeed.userservice.exception.UserRegistrationException;
import com.searchfeed.userservice.respository.UserRespository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password4j.BcryptPassword4jPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {
    private final UserRespository userRespository;
    private final BcryptPassword4jPasswordEncoder passwordEncoder;
    private final KafkaTemplate<String, UserCreatedEvent> kafkaTemplate;

    private final static String USER_CREATED_TOPIC="user.created";



    public AuthResponse register(RegisterRequest request) {
        log.info("Registering User : {}",request.getEmail());
        if(userRespository.existsByEmail(request.getEmail())){
            throw new UserRegistrationException("Email Already Exists");
        }
        User user=User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .about(request.getAbout())
                .headline(request.getHeadline())
                .location(request.getLocation())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(UserRole.ROLE_USER)
                .build();
        User savedUser=userRespository.save(user);
        log.info("User {} has been registered successfully",savedUser.getId());

        UserCreatedEvent userCreatedEvent=UserCreatedEvent.builder()
                .id(savedUser.getId())
                .firstName(savedUser.getFirstName())
                .lastName(savedUser.getLastName())
                .email(savedUser.getEmail())
                .build();
        //publish user.created event for search service
        kafkaTemplate.send(USER_CREATED_TOPIC,savedUser.getId(),userCreatedEvent)
        log.info("user.created event published :{} ",savedUser.getId());

        String token=generateToken(savedUser.getId(),savedUser.getEmail());
        return buildAuthResponse(savedUser,token);
    }

    public AuthResponse login(LoginRequest request){
        log.info("Login Request : {}",request.getEmail());
        User user=userRespository.findByEmail(request.getEmail())
                .orElseThrow(
                        ()->
                        new UserNotFoundException("No User exists for given email")
                );


        if(!passwordEncoder.matches(request.getPassword(),user.getPassword())){
            throw new AuthenticationException("Wrong Password");
        }
        log.info("Login Successfully : {}",user.getId());
        String token=generateToken(user.getId(),user.getEmail());
        return buildAuthResponse(user,token);
    }
    private AuthResponse buildAuthResponse(User user,String token){
        AuthResponse authResponse=AuthResponse.builder()
                .accessToken(token)
                .refreshToken(generateRefreshToken(user.getId()))
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }
}
