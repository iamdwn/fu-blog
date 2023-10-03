package tech.fublog.FuBlog.dto.request;

import lombok.*;
import tech.fublog.FuBlog.dto.TagDTO;

import java.util.Date;
import java.util.Set;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestBlogPostDTO {
    private Long postId;
    private String typePost;
    private String title;
    private String content;
    private String categoryName;
    private Long parentCategoryId;
    private Set<TagDTO> tagList;
    private Long userId;
    private Long views;
    private Date createDate = new Date();

    public RequestBlogPostDTO(Long postId, String typePost, String title, String content, String categoryName, Long parentCategoryId, Set<TagDTO> tagList, Long userId) {
        this.postId = postId;
        this.typePost = typePost;
        this.title = title;
        this.content = content;
        this.categoryName = categoryName;
        this.parentCategoryId = parentCategoryId;
        this.tagList = tagList;
        this.userId = userId;
    }
}
