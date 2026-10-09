package com.searchfeed.userservice.event;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ConnectionRequestedEvent {
    String senderId;
    String receiverId;
    LocalDateTime sentDateTime;
}
