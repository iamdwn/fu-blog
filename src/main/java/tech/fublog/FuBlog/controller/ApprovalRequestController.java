package tech.fublog.FuBlog.controller;


import tech.fublog.FuBlog.dto.ApprovalRequestDTO;
import tech.fublog.FuBlog.dto.BlogPostDTO;
import tech.fublog.FuBlog.entity.ApprovalRequestEntity;
import tech.fublog.FuBlog.entity.BlogPostEntity;
import tech.fublog.FuBlog.model.ResponseObject;
import tech.fublog.FuBlog.service.ApprovalRequestService;
import tech.fublog.FuBlog.service.BlogPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth/blogPosts")
@CrossOrigin(origins = "*")
public class ApprovalRequestController {

    @Autowired
    private BlogPostService blogPostService;

    @Autowired
    private ApprovalRequestService approvalRequestService;


    @PutMapping("/manageBlog/approve/{postId}")
    public ResponseEntity<ResponseObject> approveBlog(
            @PathVariable Long postId,
            @RequestBody ApprovalRequestDTO approvalRequestDTO
            ) {

        return approvalRequestService.approveBlogPost(postId, approvalRequestDTO);
    }


    @GetMapping("/manageBlog/viewApprovalRequest")
    public ResponseEntity<ResponseObject> getAllApprovalRequest() {
        return approvalRequestService.getAllApprovalRequest();
    }

}