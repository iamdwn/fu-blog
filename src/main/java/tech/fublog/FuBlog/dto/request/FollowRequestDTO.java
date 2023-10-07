package tech.fublog.FuBlog.dto.request;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FollowRequestDTO {
    private Long follower;
    private Long following;
}
