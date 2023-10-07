package tech.fublog.FuBlog.dto.request;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalRequestRequestDTO {
    private Boolean isApproved;
    private Long postId;
//    private Long requestId;
    private Long reviewId;
}
