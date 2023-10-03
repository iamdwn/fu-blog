package tech.fublog.FuBlog.dto.response;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseApprovalRequestDTO {
    //    private Boolean isApproved;
    private Long postId;
    private Long requestId;
//    private Long reviewId;
}
