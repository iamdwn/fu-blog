package tech.fublog.FuBlog.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Notification")
public class NotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long notificationId;

    @Column(columnDefinition = "LONGTEXT")
    private String content;

    @Column
    private Boolean isRead;

    @Column
    @CreatedDate
    private Date createdDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity notification;

}
