package tech.fublog.FuBlog.dto.request;

import lombok.*;

import java.util.Date;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BlogReportDTO {
    private String reason;
    private Long userId;
    private Long blogId;
    private Date createdDate;
}
