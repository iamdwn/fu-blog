package tech.fublog.FuBlog.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Objects;

@Setter
@Getter
@Entity
@Data
@NoArgsConstructor
@Table(name = "ApprovalRequest")
@EntityListeners(AuditingEntityListener.class)
public class ApprovalRequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private boolean isApproved;

    @ManyToOne
    @JoinColumn(name = "request_id")
    private UserEntity request;

    @ManyToOne
    @JoinColumn(name = "review_id")
    private UserEntity review;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private BlogPostEntity blogPost;

    public ApprovalRequestEntity(BlogPostEntity blogPost) {
        this.request = blogPost.getAuthors();
        this.blogPost = blogPost;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ApprovalRequestEntity approvalRequest = (ApprovalRequestEntity) obj;
        return Objects.equals(id, approvalRequest.getId());
    }

    public ApprovalRequestEntity(Long id, boolean isApproved, UserEntity request, UserEntity review, BlogPostEntity blogPost) {
        id = id;
        this.isApproved = isApproved;
        this.request = request;
        this.review = review;
        this.blogPost = blogPost;
    }

//    public ApprovalRequestEntity(Long id, boolean isApproved, UserEntity request, UserEntity review, BlogPostEntity blogPost) {
//        Id = id;
//        this.isApproved = isApproved;
//        this.request = request;
//        this.review = review;
//        this.blogPost = blogPost;
//    }
}

