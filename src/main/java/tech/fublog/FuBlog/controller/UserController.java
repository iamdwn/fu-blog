package tech.fublog.FuBlog.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import tech.fublog.FuBlog.auth.MessageResponse;
import tech.fublog.FuBlog.dto.BlogPostDTO;
import tech.fublog.FuBlog.dto.PostMarkDTO;
import tech.fublog.FuBlog.dto.UserDTO;
import tech.fublog.FuBlog.entity.UserEntity;
import tech.fublog.FuBlog.exception.UserException;
import tech.fublog.FuBlog.model.ResponseObject;
import tech.fublog.FuBlog.repository.UserRepository;
import tech.fublog.FuBlog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth/user")
@CrossOrigin(origins = {"http://localhost:5173", "https://fublog.tech"})
public class UserController {
    private final UserService userService;

    private final UserRepository userRepository;
    @Autowired
    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }




    @GetMapping("/getAll")
    public List<UserEntity> getAllUser(){
        return  userRepository.findAllByStatusIsTrue();
    }

    @PostMapping("/mark")
    public ResponseEntity<ResponseObject> markPost(@RequestBody PostMarkDTO postMarkDTO) {
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("ok", "found", userService.markPost(postMarkDTO.getUserId(), postMarkDTO.getPostId())));
        } catch (UserException ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }

    @GetMapping("/getMarkPost/{userId}")
    public ResponseEntity<ResponseObject> getMarkPostByUser(@PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject("ok", "found", userService.getMarkPostByUser(userId)));
    }

    @GetMapping("/getActiveUser")
    public ResponseEntity<ResponseObject> getActiveUser() {

        return userService.getActiveUser();
    }
    @GetMapping("/getUser/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable Long userId){
        UserEntity user = userRepository.findByIdAndStatusIsTrue(userId);
        if(user != null){
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Don't find user!!!!"));
    }
    @DeleteMapping("/deleteUser/{userId}")
    public ResponseEntity<ResponseObject> deleteBlog(@PathVariable Long userId) {

        return userService.deleteBlogPost(userId);
    }
    @PutMapping("/updateUser/{userId}")
    public ResponseEntity<?> updateBlog(
            @PathVariable Long userId,
            @RequestBody UserDTO userDTO
    ) {
        if (userId == null) {
            return ResponseEntity.badRequest().body("User ID cannot be null");
        }
        return userService.updateUser(userId, userDTO);
    }

}
