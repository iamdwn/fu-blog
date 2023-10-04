package tech.fublog.FuBlog.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.util.Objects;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Follow")
@EntityListeners(AuditingEntityListener.class)
public class FollowEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
