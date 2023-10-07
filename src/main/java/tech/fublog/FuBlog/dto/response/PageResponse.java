package tech.fublog.FuBlog.dto.response;

import lombok.*;
import tech.fublog.FuBlog.dto.BlogPostDTO;

import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse {
    private List<BlogPostDTO> blogPostDTOList;
    private Long blogPostCount;
    private Long pageCount;
}
