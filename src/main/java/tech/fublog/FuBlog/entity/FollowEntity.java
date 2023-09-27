package tech.fublog.FuBlog.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
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

}
