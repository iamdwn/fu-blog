package tech.fublog.FuBlog.dto.response;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseFollowDTO {
    private Long follower;
    private Long following;
}
