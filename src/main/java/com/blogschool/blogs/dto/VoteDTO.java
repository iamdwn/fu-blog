package com.blogschool.blogs.dto;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VoteDTO {
    private Long voteId;
    private Long voteValue;
    private Long postId;
    private Long userId;
}
