package com.blogschool.blogs.controller;

import com.blogschool.blogs.dto.BlogPostDTO;
import com.blogschool.blogs.entity.BlogPostEntity;
import com.blogschool.blogs.model.ResponseObject;
import com.blogschool.blogs.service.ApprovalRequestService;
import com.blogschool.blogs.service.BlogPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/blogPosts")
public class BlogPostController {

    @Autowired
    private BlogPostService blogPostService;

    @Autowired
    private ApprovalRequestService approvalRequestService;


    @GetMapping("/viewBlog")
    ResponseEntity<ResponseObject> getAllBlogPost() {

        return blogPostService.getAllBlogPosts();
    }


    @DeleteMapping("/deleteBlog/{postId}")
    public ResponseEntity<ResponseObject> deleteBlog(@PathVariable Long postId) {

        return blogPostService.deleteBlogPost(postId);
    }


    @PostMapping("/writeBlog")
    ResponseEntity<ResponseObject> insertBlogPost(@RequestBody BlogPostDTO blogPostDTO) {
        BlogPostEntity blogPostEntity = blogPostService.createBlogPost(blogPostDTO);
        if (blogPostEntity != null) {
            return approvalRequestService.createApprovalRequestById(blogPostEntity);
        }

        return null;
    }


    @PutMapping("/editBlog/{postId}")
    public ResponseEntity<ResponseObject> updateBlog(
            @PathVariable Long postId,
            @RequestBody BlogPostDTO blogPostDTO
    ) {

        return blogPostService.updateBlogPost(postId, blogPostDTO);
    }


    @GetMapping("/search/{category}")
    ResponseEntity<ResponseObject> findBlogByCategory(@PathVariable String category) {
        return blogPostService.findBlogByCategory(category);
    }


}


