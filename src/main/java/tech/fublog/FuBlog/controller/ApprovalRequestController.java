package tech.fublog.FuBlog.controller;

import org.springframework.http.HttpStatus;
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

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth/blogPosts")
@CrossOrigin(origins = "*")
public class ApprovalRequestController {

    @Autowired
    private BlogPostService blogPostService;

    @Autowired
    private ApprovalRequestService approvalRequestService;

//    private final ApprovalRequestService approvalRequestService;

    @Autowired
    public ApprovalRequestController(ApprovalRequestService approvalRequestService) {
        this.approvalRequestService = approvalRequestService;
    }

    @RequestMapping("/view")
    public ResponseEntity<ResponseObject> getAllRequest() {
        List<ApprovalRequestDTO> dtoList = approvalRequestService.getAllApprovalRequest()/*viewComment(postId)*/;
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject("ok", "found", dtoList));
    }

    @PutMapping("/manageBlog/approve/{postId}")
    public ResponseEntity<ResponseObject> approveBlog(
            @PathVariable Long postId,
            @RequestBody ApprovalRequestDTO approvalRequestDTO
            ) {

        return approvalRequestService.approveBlogPost(postId, approvalRequestDTO);
    }


//    @GetMapping("/manageBlog/viewApprovalRequest")
//    public ResponseEntity<ResponseObject> getAllApprovalRequest() {
//        return approvalRequestService.getAllApprovalRequest();
//    }

}
