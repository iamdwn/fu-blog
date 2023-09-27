package tech.fublog.FuBlog.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {


    private String username;

    @JsonIgnore
    private String password;


    private String email;


    private String fullName;


    private Long UserRole;


    public UserDTO(String username, String email, String fullName) {
        this.username = username;
        this.email = email;
        this.fullName = fullName;
//        this.UserRole = userRole;
    }
}
