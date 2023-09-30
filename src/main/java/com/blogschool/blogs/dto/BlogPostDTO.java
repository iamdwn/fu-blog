package com.blogschool.blogs.dto;

import lombok.*;

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
    private Long parentCategoryId;
    private Long userId;
}
