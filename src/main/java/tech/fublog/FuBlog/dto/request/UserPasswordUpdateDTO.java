package tech.fublog.FuBlog.dto.request;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserPasswordUpdateDTO {
    private String confirmPassword;
    private String newPassword;
}

