package com.blogschool.blogs.dto;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalRequestDTO {
    private Boolean isApproved;
    private Long postId;
    private Long requestId;
    private Long reviewId;
}
