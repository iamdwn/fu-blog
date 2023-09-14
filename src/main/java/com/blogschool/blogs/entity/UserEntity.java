package com.blogschool.blogs.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "User")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column
    private String username;

    @Column
    private String password;

    @Column
    private String email;

    @Column
    private String fullName;

    @Column
    private String userType;

    @Column
    private String status;

    @OneToMany(mappedBy = "authors")
    @JsonIgnore
    private List<BlogPostEntity> blogAuthors = new ArrayList<>();
    @OneToMany(mappedBy = "authorsModified")
    @JsonIgnore
    private List<BlogPostEntity> blogAuthorsModified = new ArrayList<>();

    @OneToMany(mappedBy = "request")
    @JsonIgnore
    private List<ApprovalRequestEntity> requested = new ArrayList<>();

    @OneToMany(mappedBy = "review")
    @JsonIgnore
    private List<ApprovalRequestEntity> reviewed = new ArrayList<>();

    @OneToMany(mappedBy = "userComment")
    @JsonIgnore
    private List<CommentEntity> comments = new ArrayList<>();

    @OneToMany(mappedBy = "userVote")
    @JsonIgnore
    private List<VoteEntity> votes = new ArrayList<>();

    @OneToMany(mappedBy = "notification")
    @JsonIgnore
    private List<NotificationEntity> notificationList = new ArrayList<>();

    @OneToMany(mappedBy = "category")
    @JsonIgnore
    private List<CategoryEntity> categories = new ArrayList<>();

    @OneToMany(mappedBy = "following")
    @JsonIgnore
    private List<FollowEntity> followingList = new ArrayList<>();

    @OneToMany(mappedBy = "follower")
    @JsonIgnore
    private List<FollowEntity> followersList = new ArrayList<>();

    @ManyToMany(mappedBy = "users")
    @JsonIgnore
    private List<AwardEntity> awards = new ArrayList<>();


    public Long getUserId() {
        return Id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public List<BlogPostEntity> getBlogAuthors() {
        return blogAuthors;
    }

    public void setBlogAuthors(List<BlogPostEntity> blogAuthors) {
        this.blogAuthors = blogAuthors;
    }

    public List<BlogPostEntity> getBlogAuthorsModified() {
        return blogAuthorsModified;
    }

    public void setBlogAuthorsModified(List<BlogPostEntity> blogAuthorsModified) {
        this.blogAuthorsModified = blogAuthorsModified;
    }

    public List<ApprovalRequestEntity> getRequested() {
        return requested;
    }

    public void setRequested(List<ApprovalRequestEntity> requested) {
        this.requested = requested;
    }

    public List<ApprovalRequestEntity> getReviewed() {
        return reviewed;
    }

    public void setReviewed(List<ApprovalRequestEntity> reviewed) {
        this.reviewed = reviewed;
    }

    public List<CommentEntity> getComments() {
        return comments;
    }

    public void setComments(List<CommentEntity> comments) {
        this.comments = comments;
    }

    public List<VoteEntity> getVotes() {
        return votes;
    }

    public void setVotes(List<VoteEntity> votes) {
        this.votes = votes;
    }

    public List<NotificationEntity> getNotificationList() {
        return notificationList;
    }

    public void setNotificationList(List<NotificationEntity> notificationList) {
        this.notificationList = notificationList;
    }

    public List<CategoryEntity> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryEntity> categories) {
        this.categories = categories;
    }

    public List<FollowEntity> getFollowingList() {
        return followingList;
    }

    public void setFollowingList(List<FollowEntity> followingList) {
        this.followingList = followingList;
    }

    public List<FollowEntity> getFollowersList() {
        return followersList;
    }

    public void setFollowersList(List<FollowEntity> followersList) {
        this.followersList = followersList;
    }

    public List<AwardEntity> getAwards() {
        return awards;
    }

    public void setAwards(List<AwardEntity> awards) {
        this.awards = awards;
    }

    public String getStatus() {return status;}

    public void setStatus(String status) {this.status = status;}
}
