package com.blogschool.blogs.controller;


import com.blogschool.blogs.entity.ApprovalRequestEntity;
import com.blogschool.blogs.entity.BlogPostEntity;
import com.blogschool.blogs.model.ResponseObject;
import com.blogschool.blogs.service.ApprovalRequestService;
import com.blogschool.blogs.service.BlogPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/blogPosts")
public class ApprovalRequestController {

    @Autowired
    private BlogPostService blogPostService;

    @Autowired
    private ApprovalRequestService approvalRequestService;

    @PutMapping("/manageBlog/approve/{postId}")
    public ResponseEntity<ResponseObject> approveBlog(
            @PathVariable Long postId,
            @RequestParam Long reviewId,
            @RequestParam String command
    ) {

        return approvalRequestService.approveBlogPost(postId, reviewId, command);
    }
}