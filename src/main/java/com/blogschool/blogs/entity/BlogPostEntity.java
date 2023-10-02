package com.blogschool.blogs.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.*;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "BlogPost")
public class BlogPostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column
    private String typePost;

    @Column
    private String title;

    @Column(columnDefinition = "LONGTEXT")
    private String content;

    @Column
    @CreatedDate
    private Date createdDate = new Date();


    @Column
    @LastModifiedDate
    private Date modifiedDate;

    @Column
    private Long approvedBy;

    @Column
    private Boolean status = true;

    @Column
    private Boolean isApproved = false;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryEntity category;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private UserEntity authors;

    @OneToMany(mappedBy = "postVote")
    private Set<VoteEntity> votes = new HashSet<>();

    @OneToMany(mappedBy = "blogPost")
    private Set<ApprovalRequestEntity> approvalRequests = new HashSet<>();

    @OneToMany(mappedBy = "postComment")
    private Set<CommentEntity> postComments = new HashSet<>();

    @OneToMany(mappedBy = "post")
    private Set<PostTagEntity> postTags = new HashSet<>();

    @ManyToMany(mappedBy = "markPosts")
    private Set<UserEntity> userMarks = new HashSet<>();

    public BlogPostEntity(String typePost, String title, String content, CategoryEntity category, UserEntity authors) {
        this.typePost = typePost;
        this.title = title;
        this.content = content;
        this.category = category;
        this.authors = authors;
    }

    @Override
    public int hashCode() {
        return Objects.hash(Id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        BlogPostEntity blogPost = (BlogPostEntity) obj;
        return Objects.equals(Id, blogPost.getId());
    }
}
