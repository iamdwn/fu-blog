package tech.fublog.FuBlog.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.util.Date;

@Entity
@Data
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
    private Date createdDate;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private UserEntity userComment;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "post_id")
    private BlogPostEntity postComment;

    public CommentEntity(String content, Date createdDate, UserEntity userComment, BlogPostEntity postComment) {
        this.content = content;
        this.createdDate = createdDate;
        this.userComment = userComment;
        this.postComment = postComment;
    }
}