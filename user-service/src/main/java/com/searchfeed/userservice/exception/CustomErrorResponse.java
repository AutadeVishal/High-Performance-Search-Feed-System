package com.searchfeed.userservice.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
public class CustomErrorResponse {
    private String title;
    private String message;
}
