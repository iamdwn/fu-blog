package tech.fublog.FuBlog.dto;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FollowDTO {
    private Long follower;
    private Long following;
}
