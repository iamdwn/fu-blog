package tech.fublog.FuBlog.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Notification")
@EntityListeners(AuditingEntityListener.class)
public class NotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    @Column(columnDefinition = "LONGTEXT")
    private String content;

    @Column
    private Boolean isRead;

    @Column
    private boolean isDelivered;

    @Column
    private Long postId;

    @Column
    @CreatedDate
    private Date createdDate;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private UserEntity userId;

}
