package tech.fublog.FuBlog.dto.response;

import lombok.*;

import java.util.Date;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {
    private Long notificationId;
    private String content;
    private Boolean isRead;
    private boolean isDelivered;
    private Long postId;
    private Date createdDate;
    private Long userId;
}
