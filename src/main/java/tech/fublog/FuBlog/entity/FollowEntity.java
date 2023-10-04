package tech.fublog.FuBlog.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Objects;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Follow")
@EntityListeners(AuditingEntityListener.class)
public class FollowEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "follower_Id")
    private UserEntity follower;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "following_Id")
    private UserEntity following;

    public FollowEntity(UserEntity follower, UserEntity following) {
        this.follower = follower;
        this.following = following;
    }

    @Override
    public int hashCode() {
        return Objects.hash(follower, following);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        FollowEntity follow = (FollowEntity) obj;
        return Objects.equals(follower, follow.getFollower()) &&
                Objects.equals(following, follow.getFollowing());
    }
}
