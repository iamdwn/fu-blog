package com.blogschool.blogs.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "Follow")
public class FollowEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long Id;



    @ManyToOne
    @JoinColumn(name = "follower_Id")
    private UserEntity follower;

    @ManyToOne
    @JoinColumn(name = "following_Id")
    @JsonIgnore
    private UserEntity following;

    public Long getFollowId() {
        return Id;
    }

    public UserEntity getFollower() {
        return follower;
    }

    public void setFollower(UserEntity follower) {
        this.follower = follower;
    }

    public UserEntity getFollowing() {
        return following;
    }

    public void setFollowing(UserEntity following) {
        this.following = following;
    }
}
