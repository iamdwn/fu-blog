package tech.fublog.FuBlog.controller;

import org.springframework.http.HttpStatus;
import tech.fublog.FuBlog.Utility.TokenChecker;
import tech.fublog.FuBlog.dto.request.ApprovalRequestRequestDTO;
import tech.fublog.FuBlog.dto.response.ApprovalRequestResponseDTO;
import tech.fublog.FuBlog.dto.response.PaginationResponseDTO;
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

        try {
            if (TokenChecker.checkRole(token, true)) {
                List<ApprovalRequestResponseDTO> dtoList = approvalRequestService.getAllApprovalRequest();
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseObject("ok", "found", dtoList));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", "not found", ""));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }

    }

    @GetMapping("/viewByCategory/{categoryId}")
    public ResponseEntity<ResponseObject> getAllRequestByCategory(@RequestHeader("Authorization") String token,
                                                                  @PathVariable Long categoryId) {

        try {
            if (TokenChecker.checkRole(token, true)) {
                PaginationResponseDTO dtoList = approvalRequestService.getAllApprovalRequestByCategory(categoryId);
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseObject("ok", "found", dtoList));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", "not found", ""));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }

    }

    @PutMapping("/approve")
    public ResponseEntity<ResponseObject> approveBlog(@RequestHeader("Authorization") String token,
                                                      @RequestBody ApprovalRequestRequestDTO approvalRequestRequestDTO) {
        try {
            if (TokenChecker.checkRole(token, true)) {
                return approvalRequestService.approveBlogPost(approvalRequestRequestDTO);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", "not found", ""));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }

}
