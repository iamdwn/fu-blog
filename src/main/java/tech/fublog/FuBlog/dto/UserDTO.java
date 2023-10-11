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
    private String fullname;
    private String password;
    private String email;
    private Long id;
    private String picture;
    private Boolean status;
    private String role;
    private List<String> roles;

}
