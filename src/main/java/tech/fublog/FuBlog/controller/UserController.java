package tech.fublog.FuBlog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.fublog.FuBlog.model.ResponseObject;
import tech.fublog.FuBlog.service.UserService;

@RestController
@RequestMapping("/api/v1/auth/user")
public class UserController {

    @Autowired
    private final UserService userService;

    public UserController(UserService userService) {this.userService = userService;}


    @GetMapping("/getActiveUser")
    public ResponseEntity<ResponseObject> getActiveUser() {

    return userService.getActiveUser();
    }
}