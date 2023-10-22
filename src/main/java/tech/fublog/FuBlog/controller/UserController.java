package tech.fublog.FuBlog.controller;

import org.springframework.web.bind.annotation.*;
import tech.fublog.FuBlog.dto.PostMarkDTO;
import tech.fublog.FuBlog.dto.UserDTO;
import tech.fublog.FuBlog.dto.response.PaginationResponseDTO;
import tech.fublog.FuBlog.entity.UserEntity;
import tech.fublog.FuBlog.exception.BlogPostException;
import tech.fublog.FuBlog.exception.UserException;
import tech.fublog.FuBlog.model.ResponseObject;
import tech.fublog.FuBlog.repository.UserRepository;
import tech.fublog.FuBlog.service.BlogPostService;
import tech.fublog.FuBlog.service.JwtService;
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
    private final BlogPostService blogPostService;

    private final JwtService jwtService;

    private final UserRepository userRepository;

    @Autowired
    public UserController(UserService userService, BlogPostService blogPostService, JwtService jwtService, UserRepository userRepository) {
        this.userService = userService;
        this.blogPostService = blogPostService;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }


    @GetMapping("/getAll/{page}/{size}")
    public ResponseEntity<ResponseObject> getAllUser(
            @PathVariable int page,
            @PathVariable int size
    ) {
        try {
            PaginationResponseDTO users = userService.getAllUsers(page, size);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("ok", "found", users));
        } catch (UserException ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }

    @PostMapping("/markAction/{action}")
    public ResponseEntity<ResponseObject> markBook(@PathVariable String action, @RequestBody PostMarkDTO postMarkDTO) {
        try {
            if (action.equals("mark")) {
                userService.markPost(postMarkDTO.getUserId(), postMarkDTO.getPostId());
            } else if (action.equals("unMark")) {
                userService.unMarkPost(postMarkDTO.getUserId(), postMarkDTO.getPostId());
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("ok", "successfully", ""));
        } catch (UserException ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }

    @PutMapping("/checkMark")
    public ResponseEntity<ResponseObject> checkMarkPost(@RequestBody PostMarkDTO postMarkDTO) {
        try {
            boolean result = userService.checkMarkPost(postMarkDTO.getUserId(), postMarkDTO.getPostId());
            if (result)
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseObject("ok", "You already marked this post", true));
            else
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseObject("ok", "You hasn't marked this post", false));
        } catch (UserException ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }

    @GetMapping("/getBlogByBookMarkUser/{userId}/{page}/{size}")
    public ResponseEntity<ResponseObject> getBlogByBookMarkUser(@PathVariable Long userId, @PathVariable int page, @PathVariable int size) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject("ok", "found", userService.getBlogByBookMarkUser(userId, page, size)));
    }

    @GetMapping("/getMarkPost/{userId}")
    public ResponseEntity<ResponseObject> getMarkPostByUser(@PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject("ok", "found", userService.getMarkPostByUser(userId)));
    }

    @GetMapping("/getActiveUser")
    public ResponseEntity<ResponseObject> getActiveUser() {
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("ok", "found", userService.getActiveUser()));
        } catch (UserException ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }


    @GetMapping("/getUser/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable Long userId) {
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("ok", "found", userService.getUserInfo(userId)));
        } catch (UserException ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }

    @DeleteMapping("/deleteUser/{userId}")
    public ResponseEntity<ResponseObject> deleteUser(@PathVariable Long userId) {
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("ok", "found", userService.deleteBlogPost(userId)));
        } catch (UserException ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }

    }

    @PutMapping("/updateUser/{userId}")
    public ResponseEntity<?> updateUser(
            @PathVariable Long userId,
            @RequestBody UserDTO userDTO
    ) {
        if (userId == null) {
            return ResponseEntity.badRequest().body("User ID cannot be null");
        }
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("ok", "found", userService.updateUser(userId, userDTO)));
        } catch (UserException ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }

    @GetMapping("/countPostMarkByUser/{userId}")
    ResponseEntity<ResponseObject> countPostMarkByUser(@PathVariable Long userId) {
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("ok", "post found", blogPostService.countPostMarkByUser(userId)));
        } catch (BlogPostException ex) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }

    @GetMapping("/countViewOfBlog/{userId}")
    public ResponseEntity<ResponseObject> countViewOfBlog(
            @PathVariable Long userId
    ) {
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("ok", "found", userService.countViewOfBlog(userId)));
        } catch (UserException ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }

    @GetMapping("/getUserBanned")
    public ResponseEntity<ResponseObject> getUserBanned(@RequestHeader("Authorization") String token) {
        List<String> roles = jwtService.extractTokenToGetRoles(token.substring(7));
        if (roles != null) {
            if (!roles.isEmpty()) {
                for (String role : roles) {
                    if (!role.toUpperCase().equals("USER")) {
                        List<UserEntity> usersBanned = userRepository.findAllByStatusIsFalse();
                        return ResponseEntity.status(HttpStatus.OK)
                                .body(new ResponseObject("ok", "found", usersBanned));
                    }
                    return ResponseEntity.status(HttpStatus.OK)
                            .body(new ResponseObject("ok", "Role is not sp!!", ""));
                }
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseObject("ok", "Role is empty!!", ""));
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("ok", "Role is null!!", ""));
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject("failed", "token is wrong or role is not sp!!", ""));
    }

    @GetMapping("/getAllUserByPoint/{page}/{size}")
    public ResponseEntity<ResponseObject> getAllUserByPoint(@PathVariable int page,
                                                            @PathVariable int size) {
        PaginationResponseDTO userList = userService.getAllUserByPoint(page, size);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject("ok", "found", userList));
    }

    @GetMapping("/getAllUserByDiamond/{page}/{size}")
    public ResponseEntity<ResponseObject> getAllUserByDiamond(@PathVariable int page,
                                                            @PathVariable int size) {
        PaginationResponseDTO userList = userService.getAllUserByAward("diamond", page, size);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject("ok", "found", userList));
    }

    @GetMapping("/getAllUserByGold/{page}/{size}")
    public ResponseEntity<ResponseObject> getAllUserByGold(@PathVariable int page,
                                                            @PathVariable int size) {
        PaginationResponseDTO userList = userService.getAllUserByAward("gold", page, size);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject("ok", "found", userList));
    }

    @GetMapping("/getAllUserBySilver/{page}/{size}")
    public ResponseEntity<ResponseObject> getAllUserBySilver(@PathVariable int page,
                                                            @PathVariable int size) {
        PaginationResponseDTO userList = userService.getAllUserByAward("silver", page, size);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject("ok", "found", userList));
    }

}
