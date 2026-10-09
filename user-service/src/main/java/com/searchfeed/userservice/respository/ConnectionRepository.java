package com.searchfeed.userservice.respository;

import com.searchfeed.userservice.entity.Connection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConnectionRepository extends JpaRepository<Connection,String> {
}
