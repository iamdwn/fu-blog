package tech.fublog.FuBlog.dto;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TagDTO {
    private Long tagId;
    private String tagName;
}
