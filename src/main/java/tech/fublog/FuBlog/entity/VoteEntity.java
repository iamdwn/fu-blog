package tech.fublog.FuBlog.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Vote")
public class VoteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long Id;

    @Column
    private Long voteValue; // 1 for upvote, -1 for downvote

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userVote;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private BlogPostEntity postVote;

}
