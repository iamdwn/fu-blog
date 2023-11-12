package tech.fublog.FuBlog.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticationResponse {
    private String token;
    private String refreshToken;
//    private String fullname;
//    private String password;
//    private String email;
//    private Long id;
//    private String picture;
}
