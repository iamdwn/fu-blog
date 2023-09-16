package com.blogschool.blogs.controller;

import com.blogschool.blogs.entity.BlogPostEntity;
import com.blogschool.blogs.entity.CategoryEntity;
import com.blogschool.blogs.entity.UserEntity;

import com.blogschool.blogs.model.ResponseObject;
import com.blogschool.blogs.service.BlogPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/blogPosts")
public class BlogPostController {

    @Autowired
    private BlogPostService blogPostService;


    @GetMapping("/viewBlog")
    ResponseEntity<ResponseObject> getAllBlogPost() {

        return blogPostService.getAllBlogPosts();
    }


    @DeleteMapping("/deleteBlog/{postId}")
    public ResponseEntity<ResponseObject> deleteBlog(@PathVariable Long postId) {

        return blogPostService.deleteBlogPost(postId);
    }


//    @PostMapping("/writeBlog")
//    public BlogPostEntity createBlog(@RequestBody blogPost) {
//
//        return blogPostService.createBlogPost(blogPost);
//    }


    //work with @RequestParam
    @PostMapping("/writeBlog")
    public ResponseEntity<ResponseObject> createBlog(
            @RequestParam String typePost,
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam Long category,
            @RequestParam Long authors
    ) {

        return blogPostService.createBlogPost(typePost, title, content,
                category, authors);
    }

    @PutMapping("/editBlog/{postId}")
    public ResponseEntity<ResponseObject> updateBlog(
            @PathVariable Long postId,
            @RequestParam String typePost,
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam Long category,
            @RequestParam Long authorsModified
    ) {

        return blogPostService.updateBlogPost(postId, typePost, title, content,
                category, authorsModified);
    }

}
