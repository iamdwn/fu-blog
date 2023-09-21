package com.blogschool.blogs.controller;

import com.blogschool.blogs.dto.BlogPostDTO;
import com.blogschool.blogs.entity.ApprovalRequestEntity;
import com.blogschool.blogs.entity.BlogPostEntity;
import com.blogschool.blogs.entity.ResponseObject;
import com.blogschool.blogs.exception.BlogPostException;
import com.blogschool.blogs.service.ApprovalRequestService;
import com.blogschool.blogs.service.BlogPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/search/category/{category}")
    ResponseEntity<ResponseObject> findBlogByCategory(@PathVariable String category) {
        try {
            List<BlogPostDTO> dtoList = blogPostService.findBlogByCategory(category);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("ok", "found", dtoList));
        } catch (BlogPostException ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }

    @GetMapping("/search/title/{title}")
    ResponseEntity<ResponseObject> findBlogByTitle(@PathVariable String title) {
        try {
            List<BlogPostDTO> dtoList = blogPostService.findBlogByTitle(title);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("ok", "found", dtoList));
        } catch (BlogPostException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }

    @PostMapping("/insert")
    ResponseEntity<ResponseObject> insertBlogPost(@RequestBody BlogPostDTO blogPostDTO) {
        try {
            BlogPostEntity blogPostEntity = blogPostService.insertBlogPost(blogPostDTO);
            approvalRequestService.insertApprovalRequest(blogPostEntity);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("ok", "post is waiting approve", ""));
        } catch (BlogPostException ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }
}
