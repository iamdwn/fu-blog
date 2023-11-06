package tech.fublog.FuBlog.dto.response;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserRankDTO {
    private Long id;
    private String fullName;
    private String picture;
    private String email;
    private String role;
    private List<String> roles;
    private Double point;
    private String ranking;
    private Long countViewOfBlog;
    private Long countVoteOfBlog;
}
