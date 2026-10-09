package com.searchfeed.userservice.respository;

import com.searchfeed.userservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRespository extends JpaRepository<User,String> {

    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);


}
