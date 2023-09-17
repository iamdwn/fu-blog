package com.blogschool.blogs.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;

@Entity
@Table(name = "Comment")
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long Id;

    @Column(columnDefinition = "LONGTEXT")
    private String content;

    @Column
    @CreatedDate
    private Date createdDate = new Date();

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private UserEntity userComment;

    @ManyToOne
    @JoinColumn(name = "post_id")
    @JsonIgnore
    private BlogPostEntity postComment;

    public Long getCommentId() {
        return Id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public UserEntity getUserComment() {
        return userComment;
    }

    public void setUserComment(UserEntity userComment) {
        this.userComment = userComment;
    }

    public BlogPostEntity getPostComment() {
        return postComment;
    }

    public void setPostComment(BlogPostEntity postComment) {
        this.postComment = postComment;
    }

    public CommentEntity() {
    }

    public CommentEntity(String content, UserEntity userComment, BlogPostEntity postComment) {
        this.content = content;
        this.userComment = userComment;
        this.postComment = postComment;
    }

}