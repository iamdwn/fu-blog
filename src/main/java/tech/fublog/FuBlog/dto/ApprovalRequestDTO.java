package tech.fublog.FuBlog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String command;
}
