package com.blogschool.blogs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogPostDTO {
    private String typePost;
    private String title;
    private String content;
    private Long category;
    private Long authors;
}
