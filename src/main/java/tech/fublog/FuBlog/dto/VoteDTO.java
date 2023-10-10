package tech.fublog.FuBlog.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VoteDTO {
    private Long voteId;
    private Long voteValue;
    private Long postId;
    private Long userId;
}
