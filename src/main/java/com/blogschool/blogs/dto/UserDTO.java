package com.blogschool.blogs.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDTO {


    private String username;


    private String password;


    private String email;


    private String fullName;


    private String userType;


}
