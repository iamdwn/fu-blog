package tech.fublog.FuBlog.dto;

import lombok.*;
import org.springframework.web.bind.annotation.RequestParam;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private Long commentId;
    private Long parentCommentId;
    private String content;
    private Long postId;
    private Long userId;

    public CommentDTO(Long commentId,  String content, Long postId, Long userId) {
        this.commentId = commentId;
        this.content = content;
        this.postId = postId;
        this.userId = userId;
    }
}
