package tech.fublog.FuBlog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FollowDTO {
    private String action;
    private Long userId;
    private Long followingUserId;
}