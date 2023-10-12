package tech.fublog.FuBlog.dto.response;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserInfoResponseDTO {
    private Long id;
    private String image;
    private String fullname;
    private String email;
    private String role;
    private List<String> roles;
    private Double point;
}
