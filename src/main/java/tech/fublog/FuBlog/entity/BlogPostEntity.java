package tech.fublog.FuBlog.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Data
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
    private Date createdDate;


    @Column
    @LastModifiedDate
    private Date modifiedDate;


    @Column
    private Long approvedBy;

    @Column
    private  Boolean status;

    @Column
    private Boolean isApproved;

    @Column
    private String image;

    @Column
    private Long views;

    @ManyToOne
//    @JsonIgnore
    @JoinColumn(name = "category_id")
    private CategoryEntity category;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "author_id")
    private UserEntity authors;

//    @ManyToOne
//    @JoinColumn(name = "authorModified_id")
//    private UserEntity authorsModified;

    @OneToMany(mappedBy = "postVote")
//    private Set<VoteEntity> votes = new HashSet<>();
    private List<VoteEntity> votes = new ArrayList<>();

    @OneToMany(mappedBy = "blogPost")
    @JsonIgnore
    private List<ApprovalRequestEntity> approvalRequests = new ArrayList<>();

    @OneToMany(mappedBy = "postComment")
//    @JsonIgnore
//    private Set<CommentEntity> postComments = new HashSet<>();
    private List<CommentEntity> postComments = new ArrayList<>();

    @OneToMany(mappedBy = "post")
//    @JsonIgnore
    private List<PostTagEntity> postTags = new ArrayList<>();

//    @ManyToMany(mappedBy = "blogPosts")
//    private Set<TagEntity> tags = new HashSet<>();


    public BlogPostEntity(String typePost, String title, String content, Date createdDate, Date modifiedDate,
                          Long approvedBy, Boolean status, Boolean isApproved,
                          CategoryEntity category, UserEntity authors) {
        this.typePost = typePost;
        this.title = title;
        this.content = content;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.approvedBy = approvedBy;
        this.status = status;
        this.isApproved = isApproved;
        this.category = category;
        this.authors = authors;
    }
}
