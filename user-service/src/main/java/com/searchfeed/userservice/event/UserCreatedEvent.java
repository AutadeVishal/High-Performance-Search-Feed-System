package com.searchfeed.userservice.event;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserCreatedEvent {
    String id;
    String firstName;
    String lastName;
    String email;

}
