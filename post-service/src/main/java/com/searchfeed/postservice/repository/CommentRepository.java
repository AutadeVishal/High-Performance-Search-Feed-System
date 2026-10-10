package com.searchfeed.postservice.repository;

import com.searchfeed.postservice.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment,Integer> {
}
