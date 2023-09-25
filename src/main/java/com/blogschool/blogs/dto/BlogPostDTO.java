package com.blogschool.blogs.dto;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BlogPostDTO {
    private String typePost;
    private String title;
    private String content;
    private String categoryName;
    private Long userId;
}
