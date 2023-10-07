package tech.fublog.FuBlog.dto.response;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BlogPostResponseDTO {
    private Long postId;
    private Long userId;
    private String typePost;
    private String title;
    private String content;
    private String categoryName;
    private Long parentCategoryId;
    private long vote;
    private List<CommentResponseDTO> comment;
}
