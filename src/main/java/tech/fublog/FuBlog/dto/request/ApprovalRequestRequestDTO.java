package tech.fublog.FuBlog.dto.request;

import lombok.*;
import tech.fublog.FuBlog.dto.TagDTO;

import java.util.List;
import java.util.Set;

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
