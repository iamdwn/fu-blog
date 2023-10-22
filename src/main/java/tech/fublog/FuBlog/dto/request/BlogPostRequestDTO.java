package tech.fublog.FuBlog.dto.request;

import lombok.*;
import tech.fublog.FuBlog.dto.TagDTO;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BlogPostRequestDTO {
    private Long postId;
    private String typePost;
    private String title;
    private String content;
    private String image;
    private String categoryName;
    private Long parentCategoryId;
    private Long userId;
    private List<TagDTO> tagList;
//    private Long views;
    private Date createDate = new Date();

    public BlogPostRequestDTO(String typePost, String title, String content, String categoryName, Long parentCategoryId, List<TagDTO> tagList, Long userId, String image) {
        this.typePost = typePost;
        this.title = title;
        this.content = content;
        this.categoryName = categoryName;
        this.parentCategoryId = parentCategoryId;
        this.userId = userId;
        this.tagList = tagList;
        this.image = image;
    }

    public BlogPostRequestDTO(Long postId, String typePost, String title, String content, String categoryName, Long parentCategoryId, Long userId, List<TagDTO> tagList) {
        this.postId = postId;
        this.typePost = typePost;
        this.title = title;
        this.content = content;
        this.categoryName = categoryName;
        this.parentCategoryId = parentCategoryId;
        this.userId = userId;
        this.tagList = tagList;
    }
}
