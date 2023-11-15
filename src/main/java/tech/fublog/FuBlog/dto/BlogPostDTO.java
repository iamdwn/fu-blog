package tech.fublog.FuBlog.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import tech.fublog.FuBlog.dto.response.UserInfoResponseDTO;
import tech.fublog.FuBlog.entity.CategoryEntity;
import tech.fublog.FuBlog.entity.CommentEntity;
import tech.fublog.FuBlog.entity.UserEntity;
import tech.fublog.FuBlog.entity.VoteEntity;

import java.util.Date;
import java.util.HashSet;
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
    private String picture;
    private String categoryName;
    private CategoryEntity parentCategoryId;
    @JsonIgnore
    private Set<TagDTO> tagList;

    private UserInfoResponseDTO user;
    private Long views;
    private Date createdDate;
    private Long voteCount;
    private Long commentCount;
    private Long bookMarkCount;
    private String status;
    private String reason;

    public BlogPostDTO(String typePost, String title, String content, String categoryName, CategoryEntity parentCategoryId, Set<TagDTO> tagList, UserInfoResponseDTO user, Long views, Date createdDate, Long voteCount, Long commentCount) {
        this.typePost = typePost;
        this.title = title;
        this.content = content;
        this.categoryName = categoryName;
        this.parentCategoryId = parentCategoryId;
        this.tagList = tagList;
        this.user = user;
        this.views = views;
        this.createdDate = createdDate;
        this.voteCount = voteCount;
        this.commentCount = commentCount;
    }
}
