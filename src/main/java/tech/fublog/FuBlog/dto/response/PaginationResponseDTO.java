package tech.fublog.FuBlog.dto.response;

import lombok.*;
import tech.fublog.FuBlog.dto.BlogPostDTO;

import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaginationResponseDTO {
    private List<?> list;
    private Long elementCount;
    private Long pageCount;
}
