package tech.fublog.FuBlog.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "Vote")
@EntityListeners(AuditingEntityListener.class)
public class VoteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column
    private Long voteValue; // 1 for upvote, -1 for downvote

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private UserEntity userVote;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "post_id")
    private BlogPostEntity postVote;

    public VoteEntity(Long voteValue, UserEntity userVote, BlogPostEntity postVote) {
        this.voteValue = voteValue;
        this.userVote = userVote;
        this.postVote = postVote;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userVote, postVote);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        VoteEntity vote = (VoteEntity) obj;
        return Objects.equals(userVote, vote.getUserVote()) &&
                Objects.equals(postVote, vote.getPostVote());
    }
}
