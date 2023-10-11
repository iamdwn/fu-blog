package tech.fublog.FuBlog.dto.response;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FollowResponseDTO {
    private Long follower;
    private Long following;
}
