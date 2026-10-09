package com.searchfeed.userservice.service;

import com.searchfeed.userservice.dto.ConnectionResponse;
import com.searchfeed.userservice.dto.UserResponse;
import com.searchfeed.userservice.entity.Connection;
import com.searchfeed.userservice.entity.ConnectionStatus;
import com.searchfeed.userservice.event.ConnectionRequestedEvent;
import com.searchfeed.userservice.respository.ConnectionRepository;
import com.searchfeed.userservice.respository.UserRespository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.sql.ConnectionEvent;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRespository userRespository;
    private final ConnectionRepository connectionRepository;

    private final KafkaTemplate<String, ConnectionRequestedEvent> kafkaTemplate;

    private static final String CONNECTION_REQUESTED_TOPIC="connection.requested";
    public ConnectionResponse sendConnectionRequest(String receiverId, String senderId){
        Connection userConnection;
        if(connectionRepository.existsBySenderIdAndReceiverId(senderId,receiverId)){
            userConnection =connectionRepository.findBySenderIdAndReceiverId(senderId,receiverId)
                    .orElseThrow(()->new RuntimeException( "Connection record exists but could not be retrieved. senderId:"+senderId+" receiverId:"+receiverId));
            return ConnectionResponse.builder()
                    .senderId(userConnection.getSenderId())
                    .receiverId(userConnection.getReceiverId())
                    .status(userConnection.getStatus())
                    .updatedAt(userConnection.getUpdatedAt())
                    .build();
        }
        userConnection=Connection.builder()
                .senderId(senderId)
                .receiverId(receiverId)
                .status(ConnectionStatus.PENDING)
                .build();
        Connection savedConnection=connectionRepository.save(userConnection);
        ConnectionRequestedEvent connectionRequestedEvent=ConnectionRequestedEvent.builder()
                .senderId(senderId)
                .receiverId(receiverId)
                .sentDateTime(savedConnection.getUpdatedAt())
                .build();
        kafkaTemplate.send(CONNECTION_REQUESTED_TOPIC,senderId, connectionRequestedEvent);
        log.info("Connection Request send :{} -> {}",senderId,receiverId);
        return ConnectionResponse.builder()
                .senderId(userConnection.getSenderId())
                .receiverId(userConnection.getReceiverId())
                .status(ConnectionStatus.PENDING)
                .updatedAt(userConnection.getUpdatedAt())
                .build();

    }
}
