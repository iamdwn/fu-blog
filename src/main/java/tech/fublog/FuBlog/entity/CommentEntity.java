package tech.fublog.FuBlog.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.util.Date;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Comment")
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long Id;

    @Column(columnDefinition = "LONGTEXT")
    private String content;

    @Column
    @CreatedDate
    private Date createdDate = new Date();

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private UserEntity userComment;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "post_id")
    private BlogPostEntity postComment;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "parent_comment_id")
    private CommentEntity parentComment;

    public CommentEntity(String content, UserEntity userComment, BlogPostEntity postComment, CommentEntity parentComment) {
        this.content = content;
        this.userComment = userComment;
        this.postComment = postComment;
        this.parentComment = parentComment;
    }

    public CommentEntity(String content, UserEntity userComment, BlogPostEntity postComment) {

    @ManyToOne
    @JoinColumn(name = "parent_comment_id")
    private CommentEntity parentComment;

    public CommentEntity(String content, UserEntity userComment, BlogPostEntity postComment, CommentEntity parentComment) {
        this.content = content;
        this.userComment = userComment;
        this.postComment = postComment;
        this.parentComment = parentComment;
    }
}