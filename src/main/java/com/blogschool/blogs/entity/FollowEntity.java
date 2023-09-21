package com.blogschool.blogs.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
    private UserEntity following;

    public FollowEntity(UserEntity follower, UserEntity following) {
        this.follower = follower;
        this.following = following;
    }
}
