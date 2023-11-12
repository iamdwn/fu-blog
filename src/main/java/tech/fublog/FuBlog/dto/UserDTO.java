package tech.fublog.FuBlog.dto;

import lombok.*;
import org.springframework.stereotype.Service;
import tech.fublog.FuBlog.entity.RoleEntity;

import javax.management.relation.Role;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDTO {
    private String fullName;
    private String password;
    private String email;
    private Long id;
    private String picture;
    private Boolean status;
    private String role;
    private List<String> roles;
    private String username;

    public UserDTO(String fullName, String password, String email, Long id, String picture, Boolean status, String role, List<String> roles) {
        this.fullName = fullName;
        this.password = password;
        this.email = email;
        this.id = id;
        this.picture = picture;
        this.status = status;
        this.role = role;
        this.roles = roles;
    }
}
