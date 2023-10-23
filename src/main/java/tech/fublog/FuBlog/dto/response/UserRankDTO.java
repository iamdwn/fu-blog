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
    private String image;
    private String email;
    private String role;
    private List<String> roles;
    private Double point;
    private Long countViewOfBlog;
    private Long countVoteOfBlog;
}
