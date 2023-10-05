package tech.fublog.FuBlog.dto.request;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestVoteDTO {
    private Long voteId;
    private Long voteValue;
    private Long postId;
    private Long userId;
}
