package com.searchfeed.userservice.event;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserCreatedEvent {
    String id;
    String firstName;
    String lastName;
    String email;
    String headline;
    String location;
    List<String> skills;

}
