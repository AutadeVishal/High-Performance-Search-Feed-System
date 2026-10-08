package com.searchfeed.userservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    @NotBlank(message="Email is required")
    String email;
    @NotBlank(message="Password is required")
    @Size(min=8,max=16,message="Password must be minimum 8 charactors")
    String password;
}
