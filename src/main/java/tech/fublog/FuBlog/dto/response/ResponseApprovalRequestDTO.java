package tech.fublog.FuBlog.dto.response;

import lombok.*;
import tech.fublog.FuBlog.entity.UserEntity;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseApprovalRequestDTO {
    //    private Boolean isApproved;
    private Long postId;
    private UserEntity request;
    private UserEntity review;
}
