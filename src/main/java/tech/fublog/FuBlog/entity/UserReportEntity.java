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
@Table(name = "UserReport")
@EntityListeners(AuditingEntityListener.class)
public class UserReportEntity {
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
    @JoinColumn(name = "reporter_id")
    private UserEntity reporterId;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "reported_user_id")
    private UserEntity reportedUserId;

    public UserReportEntity(String reason, UserEntity reporterId, UserEntity reportedUserId) {
        this.reason = reason;
        this.reporterId = reporterId;
        this.reportedUserId = reportedUserId;
    }
}
