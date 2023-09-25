package com.blogschool.blogs.dto;

import jakarta.persistence.Column;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {


    private String username;


    private String password;


    private String email;


    private String fullName;


    private Long UserRole;


}
