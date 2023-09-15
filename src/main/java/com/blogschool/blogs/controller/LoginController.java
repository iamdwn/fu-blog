package com.blogschool.blogs.controller;

import com.blogschool.blogs.dto.LoginDTO;
import com.blogschool.blogs.dto.UserDTO;
import com.blogschool.blogs.entity.UserEntity;
import com.blogschool.blogs.exception.UserException;
import com.blogschool.blogs.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/")
public class LoginController {

    @Autowired
    private UserService userService;
    @PostMapping("login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginDTO loginDTO){
        UserEntity userEntity;
        try{
            userEntity = userService.login(loginDTO.getUsername(), loginDTO.getPassword());
            return new ResponseEntity<>("Login is successful", HttpStatus.OK);
        }catch (UserException ex) {
            System.out.println(ex.getMessage());
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("signup")
    public ResponseEntity<String> signUp(@Valid @RequestBody UserDTO userDTO){
        try {
            userService.signUpUser(userDTO);
            return new ResponseEntity<>("User registered successfully", HttpStatus.OK);
        }catch (UserException ex){
            System.out.println(ex.getMessage());
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
