package tech.fublog.FuBlog.dto.response;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseCommentDTO {
    private Long commentId;
    private Long parentCommentId;
    private String content;
    private Long postId;
    private Long userId;
    private boolean status;
    private List<ResponseCommentDTO> subComment;
}
