package com.searchfeed.userservice.dto;

import com.searchfeed.userservice.entity.ConnectionStatus;
import jakarta.persistence.*;
import lombok.Builder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
@Builder
public class ConnectionResponse {
    private String senderId;
    private String receiverId;
    private ConnectionStatus status;
    private LocalDateTime updatedAt;
}
