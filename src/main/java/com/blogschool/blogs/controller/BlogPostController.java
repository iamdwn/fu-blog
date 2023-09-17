package com.blogschool.blogs.controller;

import com.blogschool.blogs.dto.BlogPostDTO;
import com.blogschool.blogs.entity.BlogPostEntity;
import com.blogschool.blogs.entity.ResponseObject;
import com.blogschool.blogs.service.ApprovalRequestService;
import com.blogschool.blogs.service.BlogPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/blogPosts/blog")
public class BlogPostController {
    private final BlogPostService blogPostService;

    private final ApprovalRequestService approvalRequestService;

    @Autowired
    public BlogPostController(BlogPostService blogPostService, ApprovalRequestService approvalRequestService) {
        this.blogPostService = blogPostService;
        this.approvalRequestService = approvalRequestService;
    }

//    @GetMapping("/view")
//    ResponseEntity<ResponseObject> findByApproved() {
//        return ResponseEntity.status(HttpStatus.OK)
//                .body(new ResponseObject("ok", "list here", blogPostService.findByApproved()));
//    }

    @GetMapping("/search/{category}")
    ResponseEntity<ResponseObject> findBlogByCategory(@PathVariable String category) {
        return blogPostService.findBlogByCategory(category);
    }

    //    @GetMapping("/search/{title}")
//    ResponseEntity<ResponseObject> findBlogByTitle(@PathVariable String title) {
//
//    }
    @PostMapping("/insert")
    ResponseEntity<ResponseObject> insertBlogPost(@RequestBody BlogPostDTO blogPostDTO) {
        BlogPostEntity blogPostEntity = blogPostService.insertBlogPost(blogPostDTO);
        if (blogPostEntity != null) {
            approvalRequestService.insertApprovalRequest(blogPostDTO.getUserId(), blogPostEntity);
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("ok", "post is waiting approve", ""));
    }
}
