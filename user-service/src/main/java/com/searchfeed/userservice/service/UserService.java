package com.searchfeed.userservice.service;

import com.searchfeed.userservice.dto.ConnectionResponse;
import com.searchfeed.userservice.dto.UserResponse;
import com.searchfeed.userservice.entity.Connection;
import com.searchfeed.userservice.entity.ConnectionStatus;
import com.searchfeed.userservice.entity.User;
import com.searchfeed.userservice.entity.UserRole;
import com.searchfeed.userservice.event.ConnectionRequestedEvent;
import com.searchfeed.userservice.respository.ConnectionRepository;
import com.searchfeed.userservice.respository.UserRespository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.sql.ConnectionEvent;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRespository userRespository;
    private final ConnectionRepository connectionRepository;

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String CONNECTION_ACCEPTED_TOPIC="connection.accepted";
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
        return toConnectionResponse(savedConnection);

    }
    public ConnectionResponse acceptConnectionRequest(String connectionId){
        Connection userConnection=connectionRepository.findById(connectionId)
                .orElseThrow(()->new RuntimeException("Connection Record Does not Exist for "+connectionId));
        userConnection.setStatus(ConnectionStatus.CONNECTED);
       Connection savedConnection= connectionRepository.save(userConnection);

       kafkaTemplate.send(CONNECTION_ACCEPTED_TOPIC,connectionId,savedConnection);
       log.info("Connection Request Accept :{}",connectionId);
        return toConnectionResponse(savedConnection);
    }
    private ConnectionResponse toConnectionResponse(Connection userConnection){
        return ConnectionResponse.builder()
                .senderId(userConnection.getSenderId())
                .receiverId(userConnection.getReceiverId())
                .status(userConnection.getStatus())
                .updatedAt(userConnection.getUpdatedAt())
                .build();
    }
    public Page<UserResponse> getConnections(String userId, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Connection> connectionPage =
                connectionRepository.findConnectedUsers(
                        userId, ConnectionStatus.CONNECTED, pageable);

        List<String> userIds = connectionPage.getContent().stream()
                .map(c -> c.getSenderId().equals(userId)
                        ? c.getReceiverId()
                        : c.getSenderId())
                .toList();

        List<User> users = userRespository.findAllById(userIds);

        return new PageImpl<>(
                users.stream()
                        .map(this::toUserResponse)
                        .toList(),
                pageable,
                connectionPage.getTotalElements()
        );
    }
    private UserResponse toUserResponse(User user){
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .headline(user.getHeadline())
                .about(user.getAbout())
                .location(user.getLocation())
                .profilePhotoURL(user.getProfilePhotoURL())
                .coverPhotoURL(user.getCoverPhotoURL())
                .role(user.getRole())
                .skills(user.getSkills())
                .createdAt(user.getCreatedAt())
                .build();
    }


}
