package com.searchfeed.postservice.repository;

import com.searchfeed.postservice.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Integer> {
}
