package tech.fublog.FuBlog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoteDTO {
    private Long voteId;
    private Long voteValue;
    private Long userId;
}
