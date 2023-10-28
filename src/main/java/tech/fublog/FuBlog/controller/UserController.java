package tech.fublog.FuBlog.controller;

import org.springframework.web.bind.annotation.*;
import tech.fublog.FuBlog.Utility.TokenChecker;
import tech.fublog.FuBlog.dto.PostMarkDTO;
import tech.fublog.FuBlog.dto.UserDTO;
import tech.fublog.FuBlog.dto.response.PaginationResponseDTO;
import tech.fublog.FuBlog.entity.RoleEntity;
import tech.fublog.FuBlog.entity.UserEntity;
import tech.fublog.FuBlog.model.ResponseObject;
import tech.fublog.FuBlog.repository.RoleRepository;
import tech.fublog.FuBlog.repository.UserRepository;
import tech.fublog.FuBlog.service.BlogPostService;
import tech.fublog.FuBlog.service.JwtService;
import tech.fublog.FuBlog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<ResponseObject> getAllUser(@RequestHeader("Authorization") String token,
                                                     @PathVariable int page,
                                                     @PathVariable int size) {
        try {
            if (TokenChecker.checkToken(token))
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseObject("ok", "found", userService.getAllUsers(page, size)));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", "not found", ""));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }

    @PostMapping("/markAction/{action}")
    public ResponseEntity<ResponseObject> markBook(@RequestHeader("Authorization") String token,
                                                   @PathVariable String action,
                                                   @RequestBody PostMarkDTO postMarkDTO) {

        try {
            if (TokenChecker.checkToken(token)) {
                if (action.equals("mark")) {
                    userService.markPost(postMarkDTO.getUserId(), postMarkDTO.getPostId());
                } else if (action.equals("unMark")) {
                    userService.unMarkPost(postMarkDTO.getUserId(), postMarkDTO.getPostId());
                }
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseObject("ok", "successfully", ""));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", "not found", ""));
        } catch (RuntimeException ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }


    @PutMapping("/checkMark")
    public ResponseEntity<ResponseObject> checkMarkPost(@RequestHeader("Authorization") String token,
                                                        @RequestBody PostMarkDTO postMarkDTO) {
        try {
            if (TokenChecker.checkToken(token)) {
                boolean result = userService.checkMarkPost(postMarkDTO.getUserId(), postMarkDTO.getPostId());
                if (result)
                    return ResponseEntity.status(HttpStatus.OK)
                            .body(new ResponseObject("ok", "You already marked this post", true));
                else
                    return ResponseEntity.status(HttpStatus.OK)
                            .body(new ResponseObject("ok", "You hasn't marked this post", false));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", "not found", ""));
        } catch (RuntimeException ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }

    @GetMapping("/getBlogByBookMarkUser/{userId}/{page}/{size}")
    public ResponseEntity<ResponseObject> getBlogByBookMarkUser(@RequestHeader("Authorization") String token,
                                                                @PathVariable Long userId,
                                                                @PathVariable int page, @PathVariable int size) {
        try {
            if (TokenChecker.checkToken(token))
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseObject("ok", "found", userService.getBlogByBookMarkUser(userId, page, size)));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", "not found", ""));
        } catch (RuntimeException ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }

    @GetMapping("/getMarkPost/{userId}")
    public ResponseEntity<ResponseObject> getMarkPostByUser(@RequestHeader("Authorization") String token,
                                                            @PathVariable Long userId) {
        try {
            if (TokenChecker.checkToken(token))
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseObject("ok", "found", userService.getMarkPostByUser(userId)));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", "not found", ""));
        } catch (RuntimeException ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }

    @GetMapping("/getActiveUser")
    public ResponseEntity<ResponseObject> getActiveUser(@RequestHeader("Authorization") String token) {

        try {
            if (TokenChecker.checkToken(token))
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseObject("ok", "found", userService.getActiveUser()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", "not found", ""));
        } catch (RuntimeException ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }

    }


    @GetMapping("/getUser/{userId}")
    public ResponseEntity<?> getUserById(@RequestHeader("Authorization") String token,
                                         @PathVariable Long userId) {

        try {
            if (TokenChecker.checkToken(token))
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseObject("ok", "found", userService.getUserInfo(userId)));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", "not found", ""));
        } catch (RuntimeException ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }

    @DeleteMapping("/deleteUser/{userId}")
    public ResponseEntity<ResponseObject> deleteUser(@RequestHeader("Authorization") String token,
                                                     @PathVariable Long userId) {

        try {
            if (TokenChecker.checkRole(token, true)) {
                Optional<UserEntity> userEntity = userRepository.findById(userId);
                if (userEntity.isPresent()
                        && userEntity.get().getStatus())
                    userService.deleteUser(userId);
                blogPostService.deleteBlogPostByUser(userEntity.get());
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseObject("ok", "found", ""));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", "not found", ""));
        } catch (RuntimeException ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }

    @PutMapping("/updateUser/{userId}")
    public ResponseEntity<?> updateUser(@RequestHeader("Authorization") String token,
                                        @PathVariable Long userId,
                                        @RequestBody UserDTO userDTO) {
        if (userId == null) {
            return ResponseEntity.badRequest().body("User ID cannot be null");
        }
        try {
            if (TokenChecker.checkToken(token))
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseObject("ok", "found", userService.updateUser(userId, userDTO)));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", "not found", ""));
        } catch (RuntimeException ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }

    @GetMapping("/countPostMarkByUser/{userId}")
    ResponseEntity<ResponseObject> countPostMarkByUser(@RequestHeader("Authorization") String token,
                                                       @PathVariable Long userId) {
        try {
            if (TokenChecker.checkToken(token))
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseObject("ok", "post found", blogPostService.countPostMarkByUser(userId)));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", "not found", ""));
        } catch (RuntimeException ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }

    @GetMapping("/countViewOfBlog/{userId}")
    public ResponseEntity<ResponseObject> countViewOfBlog(@RequestHeader("Authorization") String token,
                                                          @PathVariable Long userId) {
        try {
            if (TokenChecker.checkToken(token))
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseObject("ok", "post found", userService.countViewOfBlog(userId, false)));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", "not found", ""));
        } catch (RuntimeException ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }

    @GetMapping("/countVoteOfBlog/{userId}")
    public ResponseEntity<ResponseObject> countVoteOfBlog(@RequestHeader("Authorization") String token,
                                                          @PathVariable Long userId) {
        try {
            if (TokenChecker.checkToken(token))
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseObject("ok", "post found", userService.countVoteOfBlog(userId, false)));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", "not found", ""));
        } catch (RuntimeException ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }

    @GetMapping("/getUserBanned")
    public ResponseEntity<ResponseObject> getUserBanned(@RequestHeader("Authorization") String token) {
        try {
            if (TokenChecker.checkRole(token, true)) {
                List<UserEntity> usersBanned = userRepository.findAllByStatusIsFalse();
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseObject("ok", "found", usersBanned));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", "not found", ""));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
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

    @PostMapping("/setRole/{userId}/{role}")
    public ResponseEntity<ResponseObject> setRole(@RequestHeader("Authorization") String token,
                                                  @PathVariable Long userId,
                                                  @PathVariable String role) {
        try {
            if (TokenChecker.checkRole(token, false)) {
                Optional<UserEntity> userEntity = userRepository.findById(userId);
                if (userEntity.isPresent()
                        && userEntity.get().getStatus())
                    userService.setRole(userEntity.get(), role);
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseObject("ok", "found", ""));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", "not found", ""));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }

}
