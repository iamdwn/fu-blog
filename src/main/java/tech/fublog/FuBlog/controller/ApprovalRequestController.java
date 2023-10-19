package tech.fublog.FuBlog.controller;

import org.springframework.http.HttpStatus;
import tech.fublog.FuBlog.dto.request.ApprovalRequestRequestDTO;
import tech.fublog.FuBlog.dto.response.ApprovalRequestResponseDTO;
import tech.fublog.FuBlog.entity.UserEntity;
import tech.fublog.FuBlog.model.ResponseObject;
import tech.fublog.FuBlog.repository.UserRepository;
import tech.fublog.FuBlog.service.ApprovalRequestService;
import tech.fublog.FuBlog.service.BlogPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.fublog.FuBlog.service.JwtService;
import tech.fublog.FuBlog.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth/manageApprove")
@CrossOrigin(origins = {"http://localhost:5173", "https://fublog.tech"})
//@CrossOrigin(origins = "*")
public class ApprovalRequestController {

    private final BlogPostService blogPostService;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    private final ApprovalRequestService approvalRequestService;

    @Autowired
    public ApprovalRequestController(BlogPostService blogPostService, UserService userService, UserRepository userRepository, JwtService jwtService, ApprovalRequestService approvalRequestService) {
        this.blogPostService = blogPostService;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.approvalRequestService = approvalRequestService;
    }

    @GetMapping("/view")
    public ResponseEntity<ResponseObject> getAllRequest(@RequestHeader("Authorization") String token) {

        List<String> roles = jwtService.extractTokenToGetRoles(token.substring(7));
        if (roles != null) {
            if (!roles.isEmpty()) {
                for (String role : roles) {
                    if(!role.toUpperCase().equals("USER")){
                        List<ApprovalRequestResponseDTO> dtoList = approvalRequestService.getAllApprovalRequest();
                        return ResponseEntity.status(HttpStatus.OK)
                                .body(new ResponseObject("ok", "found", dtoList));
                    }
                    return ResponseEntity.status(HttpStatus.OK)
                            .body(new ResponseObject("ok", "Role is not sp!!", "" ));
                }
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseObject("ok", "Role is empty!!", "" ));
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("ok", "Role is null!!", "" ));
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject("failed", "token is wrong or role is not sp!!", ""));

    }

    @PutMapping("/approve")
    public ResponseEntity<ResponseObject> approveBlog(@RequestHeader("Authorization") String token,
            @RequestBody ApprovalRequestRequestDTO approvalRequestRequestDTO) {


        List<String> roles = jwtService.extractTokenToGetRoles(token.substring(7));
        if (roles != null) {
            if (!roles.isEmpty()) {
                for (String role : roles) {
                    if(!role.toUpperCase().equals("USER")){
                        return approvalRequestService.approveBlogPost(approvalRequestRequestDTO);
                    }
                    return ResponseEntity.status(HttpStatus.OK)
                            .body(new ResponseObject("ok", "Role is not sp!!", "" ));
                }
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseObject("ok", "Role is empty!!", "" ));
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("ok", "Role is null!!", "" ));
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject("failed", "token is wrong or role is not sp!!", ""));

    }

}
