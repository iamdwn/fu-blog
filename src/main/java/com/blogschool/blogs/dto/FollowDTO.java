package com.blogschool.blogs.dto;

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
