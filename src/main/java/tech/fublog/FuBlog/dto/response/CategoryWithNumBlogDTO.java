package tech.fublog.FuBlog.dto.response;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryWithNumBlogDTO {
    private int numBlog;
    private String categoryName;
}
