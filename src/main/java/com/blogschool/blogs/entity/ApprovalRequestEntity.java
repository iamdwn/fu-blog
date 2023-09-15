package com.blogschool.blogs.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "ApprovalRequest")
public class ApprovalRequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column
    private boolean isApproved;

    @ManyToOne
    @JoinColumn(name = "request_id")
    @JsonIgnore
    private UserEntity request;

    @ManyToOne
    @JoinColumn(name = "review_id")
    @JsonIgnore
    private UserEntity review;

    @ManyToOne
    @JoinColumn(name = "post_id")
    @JsonIgnore
    private BlogPostEntity blogPost;

    public Long getId() {return Id;}

    public void setId(Long id) {Id = id;}

    public boolean isApproved() {return isApproved;}

    public void setApproved(boolean approved) {isApproved = approved;}

    public UserEntity getRequest() {return request;}

    public void setRequest(UserEntity request) {this.request = request;}

    public UserEntity getReview() {return review;}

    public void setReview(UserEntity review) {this.review = review;}

    public BlogPostEntity getBlogPost() {return blogPost;}

    public void setBlogPost(BlogPostEntity blogPost) {this.blogPost = blogPost;}

    public ApprovalRequestEntity() {
    }

    public ApprovalRequestEntity(boolean isApproved, UserEntity request, UserEntity review, BlogPostEntity blogPost) {
        this.isApproved = isApproved;
        this.request = request;
        this.review = review;
        this.blogPost = blogPost;
    }
}

