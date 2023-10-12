package tech.fublog.FuBlog.dto.response;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserResponseDTO {
    private String fullname;
    //    private String password;
    private String email;
    private Long id;
    private String picture;
    private Boolean status;
    private String role;
    private List<String> roles;
}
