package tech.fublog.FuBlog.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ApprovalRequest")
public class ApprovalRequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column
    private boolean isApproved;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "request_id")
    private UserEntity request;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "review_id")
    private UserEntity review;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "post_id")
    private BlogPostEntity blogPost;



}

