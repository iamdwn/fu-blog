package com.blogschool.blogs.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

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
    private  Boolean status;


    public UserEntity(String username, String password, String email, String fullName, String userType, Boolean status) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.fullName = fullName;
        this.userType = userType;
        this.status = status;
    }

    public UserEntity() {

    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }



    @OneToMany(mappedBy = "authors")
    private List<BlogPostEntity> blogAuthors = new ArrayList<>();
    @OneToMany(mappedBy = "authorsModified")
    private List<BlogPostEntity> blogAuthorsModified = new ArrayList<>();

    @OneToMany(mappedBy = "request")
    private List<ApprovalRequestEntity> requested = new ArrayList<>();

    @OneToMany(mappedBy = "review")
    private List<ApprovalRequestEntity> reviewed = new ArrayList<>();

    @OneToMany(mappedBy = "userComment")
    private List<CommentEntity> comments = new ArrayList<>();

    @OneToMany(mappedBy = "userVote")
    private List<VoteEntity> votes = new ArrayList<>();

    @OneToMany(mappedBy = "notification")
    private List<NotificationEntity> notificationList = new ArrayList<>();

    @OneToMany(mappedBy = "category")
    private List<CategoryEntity> categories = new ArrayList<>();

    @OneToMany(mappedBy = "following")
    private List<FollowEntity> followingList = new ArrayList<>();

    @OneToMany(mappedBy = "follower")
    private List<FollowEntity> followersList = new ArrayList<>();

    @ManyToMany(mappedBy = "users")
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
}
