package com.searchfeed.userservice.respository;

import com.searchfeed.userservice.entity.Connection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConnectionRepository extends JpaRepository<Connection,String> {
    Optional<Connection> findBySenderIdAndReceiverId(String senderId, String receiverId);
    Boolean existsBySenderIdAndReceiverId(String requesterId,String receiverId);
}
