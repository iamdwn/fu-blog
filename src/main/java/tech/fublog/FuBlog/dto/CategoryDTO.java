package com.blogschool.blogs.dto;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {
    private Long categoryId;
    private String categoryName;
    //    private Long parentCategoryId;
    private List<CategoryDTO> subcategory;
}
