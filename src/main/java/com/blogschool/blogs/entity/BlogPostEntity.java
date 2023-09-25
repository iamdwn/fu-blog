package com.blogschool.blogs.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.*;

@Entity
@Data
@Table(name = "BlogPost")
public class BlogPostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column
    private String typePost;

    @Column
    private String title;

    @Column(columnDefinition = "LONGTEXT")
    private String content;

    @Column
    @CreatedDate
    private Date createdDate = new Date();


    @Column
    @LastModifiedDate
    private Date modifiedDate;

    @Column
    private Long approvedBy;

    @Column
    private Boolean status = true;

    @Column
    private Boolean isApproved = false;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryEntity category;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private UserEntity authors;

//    @ManyToOne
//    @JoinColumn(name = "authorModified_id")
//    private UserEntity authorsModified;

    @OneToMany(mappedBy = "postVote")
    private Set<VoteEntity> votes = new HashSet<>();

    @OneToMany(mappedBy = "blogPost")
    private Set<ApprovalRequestEntity> approvalRequests = new HashSet<>();

    @OneToMany(mappedBy = "postComment")
    private Set<CommentEntity> postComments = new HashSet<>();

    @OneToMany(mappedBy = "post")
    private Set<PostTagEntity> postTags = new HashSet<>();

//    @ManyToMany(mappedBy = "blogPosts")
//    private Set<TagEntity> tags = new HashSet<>();


    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getTypePost() {
        return typePost;
    }

    public void setTypePost(String typePost) {
        this.typePost = typePost;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Long getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(Long approvedBy) {
        this.approvedBy = approvedBy;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Boolean getApproved() {
        return isApproved;
    }

    public void setApproved(Boolean approved) {
        isApproved = approved;
    }

    public CategoryEntity getCategory() {
        return category;
    }

    public void setCategory(CategoryEntity category) {
        this.category = category;
    }

    public UserEntity getAuthors() {
        return authors;
    }

    public void setAuthors(UserEntity authors) {
        this.authors = authors;
    }

    public Set<VoteEntity> getVotes() {
        return votes;
    }

    public void setVotes(Set<VoteEntity> votes) {
        this.votes = votes;
    }

    public Set<ApprovalRequestEntity> getApprovalRequests() {
        return approvalRequests;
    }

    public void setApprovalRequests(Set<ApprovalRequestEntity> approvalRequests) {
        this.approvalRequests = approvalRequests;
    }

    public Set<CommentEntity> getPostComments() {
        return postComments;
    }

    public void setPostComments(Set<CommentEntity> postComments) {
        this.postComments = postComments;
    }

    public Set<PostTagEntity> getPostTags() {
        return postTags;
    }

    public void setPostTags(Set<PostTagEntity> postTags) {
        this.postTags = postTags;
    }

    public BlogPostEntity() {
    }

    public BlogPostEntity(String typePost, String title, String content, CategoryEntity category, UserEntity authors) {
        this.typePost = typePost;
        this.title = title;
        this.content = content;
        this.category = category;
        this.authors = authors;
    }
}
