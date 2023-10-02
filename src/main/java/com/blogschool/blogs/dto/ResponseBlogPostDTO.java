package com.blogschool.blogs.dto;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseBlogPostDTO {
    private Long postId;
    private Long userId;
    private String typePost;
    private String title;
    private String content;
    private String categoryName;
    private Long parentCategoryId;
    private long vote;
    private List<ResponseCommentDTO> comment;
}
