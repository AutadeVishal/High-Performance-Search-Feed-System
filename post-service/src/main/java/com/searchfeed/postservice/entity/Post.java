package com.searchfeed.postservice.entity;

import jakarta.persistence.*;
import jdk.jfr.DataAmount;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="posts")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy= GenerationType.UUID)
    @Column(name="id")
    private String id;
    @Column(nullable=false)
    private String authorId;
    @Column(nullable=false,length=200)
    private String title;
    @Column(length=400)
    private String content;
    @ElementCollection(fetch=FetchType.EAGER)
    @CollectionTable(
            name="post-images",
            joinColumns = @JoinColumn(name="post_id")
    )
    private List<String> imageURL;
    private int likeCount;
    private int commentCount;
    @CreationTimestamp
    @Column(updatable=false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
