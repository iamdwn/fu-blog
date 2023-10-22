package tech.fublog.FuBlog.dto.request;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserReportDTO {
    private String reason;
    private Long reporterId;
    private Long reportedUserId;
}
