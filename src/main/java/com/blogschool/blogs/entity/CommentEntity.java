package com.blogschool.blogs.entity;

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
    private Date createdDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userComment;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private BlogPostEntity postComment;
    
    public CommentEntity(String content, UserEntity userComment, BlogPostEntity postComment) {
        this.content = content;
        this.userComment = userComment;
        this.postComment = postComment;
    }
}