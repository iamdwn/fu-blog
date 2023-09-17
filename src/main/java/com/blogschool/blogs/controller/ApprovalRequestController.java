package com.blogschool.blogs.controller;

import com.blogschool.blogs.entity.ResponseObject;
import com.blogschool.blogs.service.ApprovalRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/blogPosts/approve")
public class ApprovalRequestController {
    private final ApprovalRequestService approvalRequestService;

    @Autowired
    public ApprovalRequestController(ApprovalRequestService approvalRequestService) {
        this.approvalRequestService = approvalRequestService;
    }

    @RequestMapping("/view")
    public ResponseEntity<ResponseObject> getAllRequest() {
        return approvalRequestService.getAllApprovalRequest()/*viewComment(postId)*/;
    }
}
