package com.searchfeed.postservice.repository;

import com.searchfeed.postservice.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like,Integer> {
}
