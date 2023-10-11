package tech.fublog.FuBlog.dto;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {
    private Long categoryId;
    private String categoryName;
    private Long parentCategoryId;
}
