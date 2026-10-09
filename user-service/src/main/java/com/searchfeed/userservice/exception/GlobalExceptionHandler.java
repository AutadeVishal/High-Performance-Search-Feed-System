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
}
