package com.searchfeed.userservice.respository;

import com.searchfeed.userservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRespository extends JpaRepository<User,String> {

    boolean existsByEmail(String email);
}
