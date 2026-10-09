package com.searchfeed.userservice.controller;

import com.searchfeed.userservice.dto.ConnectionResponse;
import com.searchfeed.userservice.dto.UserResponse;
import com.searchfeed.userservice.exception.ProfileDataUpdateException;
import com.searchfeed.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserProfile(
            @PathVariable String userId,
            @RequestHeader("X-User_Id") String senderId
    ) {
        log.info("Get Profile  : {} requested by : {} ",userId,senderId);
        return ResponseEntity.ok(userService.getUserProfile(userId);

    }

    @PutMapping("/profile/{userId}")
    public ResponseEntity<UserResponse> updateProfile(
            @PathVariable String userId,
            @RequestHeader("X-User_Id") String senderId
    ){
        if(!userId.equals(senderId)){
            throw new ProfileDataUpdateException("Cannot Change profile of other users");
        }
        return ResponseEntity.ok(userService.updateProfile(userid,senderId);
    }

    @PostMapping("/connection/connect/{targetUserId}")
    public ResponseEntity<ConnectionResponse> sendConnectionRequest(
            @PathVariable String targetUserId,
            @RequestHeader("X-User_Id") String senderId
    ){
        return ResponseEntity.ok(userService.sendConnectionRequest(
                targetUserId,senderId
        ));
    }

    @PutMapping("/connection/accept/{connectionId}")
    public ResponseEntity<ConnectionResponse>acceptConnectionRequest(
            @PathVariable String connectionId,
            @RequestHeader("X-User_Id") String senderId
    ){
        return ResponseEntity.ok(
        userService.acceptConnectionRequest(connectionId)
        );
    }

    @GetMapping("/connections/{userId}")
    public ResponseEntity<List<UserResponse>> getConnections(
        @PathVariable String userId
    ){

    }
}
