package tech.fublog.FuBlog.dto.response;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponseDTO {
    private Long categoryId;
    private String categoryName;
    //    private Long parentCategoryId;
    private List<CategoryResponseDTO> subCategory;
}
