package com.blogschool.blogs.controller;

import com.blogschool.blogs.entity.BlogPostEntity;
import com.blogschool.blogs.entity.CategoryEntity;
import com.blogschool.blogs.entity.UserEntity;
import com.blogschool.blogs.repository.BlogPostRepository;
import com.blogschool.blogs.service.BlogPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.List;

@RestController
@RequestMapping("/api/blogPosts")
public class BlogPostController {

    @Autowired
    private BlogPostService blogPostService;


    @GetMapping("/viewBlog")
    List<BlogPostEntity> getAllBlogPost() {
        return blogPostService.getAllBlogPosts();
    }


    @PostMapping("/writeBlog")
    public BlogPostEntity createBlog(
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
    public BlogPostEntity updateBlog(
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

    @DeleteMapping("/deleteBlog/{postId}")
    public ResponseEntity<String> deleteBlog(@PathVariable Long postId) {

        if (blogPostService.deleteBlogPost(postId)) {
            return ResponseEntity.ok("BlogPost with ID " + postId + " has been deleted.");
        } else {
            return ResponseEntity.ok("NOT FOUND with ID " + postId);
        }
    }

}
