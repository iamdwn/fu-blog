package tech.fublog.FuBlog.dto;

import lombok.*;
import tech.fublog.FuBlog.entity.CategoryEntity;
import tech.fublog.FuBlog.entity.UserEntity;

import java.util.Date;
import java.util.Set;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BlogPostDTO {
    private Long postId;
    private String typePost;
    private String title;
    private String content;
    private String categoryName;
    private CategoryEntity parentCategoryId;
    private Set<TagDTO> tagList;
    private UserDTO user;
    private Long views;
    private Date createDate = new Date();

//    public BlogPostDTO(Long postId, String typePost, String title, String content, String categoryName, Long parentCategoryId, Set<TagDTO> tagList, UserDTO user) {
//        this.postId = postId;
//        this.typePost = typePost;
//        this.title = title;
//        this.content = content;
//        this.categoryName = categoryName;
//        this.parentCategoryId = parentCategoryId;
//        this.tagList = tagList;
//        this.user = user;
//    }

}
