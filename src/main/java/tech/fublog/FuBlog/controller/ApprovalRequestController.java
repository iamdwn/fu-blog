package tech.fublog.FuBlog.controller;

import org.springframework.http.HttpStatus;
import tech.fublog.FuBlog.dto.request.RequestApprovalRequestDTO;
import tech.fublog.FuBlog.dto.response.ResponseApprovalRequestDTO;
import tech.fublog.FuBlog.model.ResponseObject;
import tech.fublog.FuBlog.service.ApprovalRequestService;
import tech.fublog.FuBlog.service.BlogPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth/manageApprove")
@CrossOrigin(origins = {"http://localhost:5173", "https://fublog.tech"})
//@CrossOrigin(origins = "*")
public class ApprovalRequestController {

    private final BlogPostService blogPostService;

    private final ApprovalRequestService approvalRequestService;

    @Autowired
    public ApprovalRequestController(BlogPostService blogPostService, ApprovalRequestService approvalRequestService) {
        this.blogPostService = blogPostService;
        this.approvalRequestService = approvalRequestService;
    }

    @GetMapping("/view")
    public ResponseEntity<ResponseObject> getAllRequest() {
        List<ResponseApprovalRequestDTO> dtoList = approvalRequestService.getAllApprovalRequest();
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject("ok", "found", dtoList));
    }

    @PutMapping("/approve")
    public ResponseEntity<ResponseObject> approveBlog(
            @RequestBody RequestApprovalRequestDTO requestApprovalRequestDTO) {

        return approvalRequestService.approveBlogPost(requestApprovalRequestDTO);
    }

}
