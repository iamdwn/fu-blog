package tech.fublog.FuBlog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalRequestDTO {
    private Long reviewId;
    private String command;
}
