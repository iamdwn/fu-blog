package tech.fublog.FuBlog.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequest {
    private String fullName;

    private String username;

    private String email;

    private String password = "1";

    private String picture;

    private Set<String> role;
}
