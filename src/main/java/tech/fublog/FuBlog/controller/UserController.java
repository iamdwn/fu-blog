package tech.fublog.FuBlog.controller;

import org.springframework.web.bind.annotation.*;
import tech.fublog.FuBlog.dto.PostMarkDTO;
import tech.fublog.FuBlog.exception.UserException;
import tech.fublog.FuBlog.model.ResponseObject;
import tech.fublog.FuBlog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/blogPosts/user")
@CrossOrigin(origins = "*")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/markAction/{action}")
    public ResponseEntity<ResponseObject> markBook(@PathVariable String action, @RequestBody PostMarkDTO postMarkDTO) {
        try {
            String result = "";
            if (action.equals("mark")) {
                result = userService.markPost(postMarkDTO.getUserId(), postMarkDTO.getPostId());
            } else if (action.equals("unMark")) {
                result = userService.unMarkPost(postMarkDTO.getUserId(), postMarkDTO.getPostId());
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("ok", "successfully", result));
        } catch (UserException ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }

    @GetMapping("/getActiveUser")
    public ResponseEntity<ResponseObject> getActiveUser() {

        return userService.getActiveUser();
    }
}
