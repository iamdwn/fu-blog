package tech.fublog.FuBlog.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@Table(name = "BlogReport")
@EntityListeners(AuditingEntityListener.class)
public class BlogPostReportEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;

    @Column
    private String reason;

    @Column
    @CreatedDate
    private Date createdDate = new Date();

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "reporter_blog_id")
    private UserEntity user;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "reported_blog_id")
    private BlogPostEntity blog;


}
