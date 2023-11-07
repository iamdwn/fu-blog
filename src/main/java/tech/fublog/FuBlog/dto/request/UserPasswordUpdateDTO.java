package tech.fublog.FuBlog.dto.request;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserPasswordUpdateDTO {
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
}

