package tech.fublog.FuBlog.dto.request;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserUpdateDTO {
    private String fullName;
    private String email;
    private String picture;
}

