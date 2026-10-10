package com.searchfeed.userservice.respository;

import com.searchfeed.userservice.entity.Connection;
import com.searchfeed.userservice.entity.ConnectionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ConnectionRepository extends JpaRepository<Connection,String> {
    Optional<Connection> findBySenderIdAndReceiverId(String senderId, String receiverId);
    Boolean existsBySenderIdAndReceiverId(String requesterId,String receiverId);
    Optional<Connection> findById(String id);
    @Query("""
    SELECT c FROM Connection c
    WHERE c.status = :status
      AND (c.senderId = :userId OR c.receiverId = :userId)
    """)
    Page<Connection> findConnectedUsers(
            @Param("userId") String userId,
            @Param("status") ConnectionStatus status,
            Pageable pageable
    );
}
