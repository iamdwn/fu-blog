package tech.fublog.FuBlog.dto.request;

import lombok.*;

import java.util.Date;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserReportDTO {
    private String reason;
    private Long reporterId;
    private Long reportedUserId;
    private Date createdDate = new Date();
}
