package com.searchfeed.userservice.dto;

import com.searchfeed.userservice.entity.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private String headline;
    private String about;
    private String location;
    private String profilePhotoURL;
    private String coverPhotoURL;
    private UserRole role;
    private List<String> skills;
    private LocalDateTime createdAt;
}
