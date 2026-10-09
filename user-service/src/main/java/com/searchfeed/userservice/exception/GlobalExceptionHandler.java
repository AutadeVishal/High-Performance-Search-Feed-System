package com.searchfeed.userservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(UserRegistrationException.class)
    public ResponseEntity<CustomErrorResponse> handleUserRegistrationException(UserRegistrationException e){
        log.warn("Error in User Registration :{}",e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new CustomErrorResponse("Account Already Exists",
                        e.getMessage()));
    }
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> handleUserNotFoundException(UserNotFoundException e){
        log.warn("Error in User Login :{}",e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new CustomErrorResponse("User not found",e.getMessage()));
    }
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<CustomErrorResponse> handleException(AuthenticationException e){
        log.warn("Error in User Authentication :{}",e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new CustomErrorResponse("Authentication Failed",e.getMessage()));
    }
    @ExceptionHandler(ProfileDataUpdateException.class)
    public ResponseEntity<CustomErrorResponse> handleException(ProfileDataUpdateException e){
        log.warn("Error in Profile Data Update :{}",e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new CustomErrorResponse("Profile Data Update Failed",e.getMessage()));
    }
}
