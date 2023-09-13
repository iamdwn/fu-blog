package com.blogschool.blogs.controller;

import com.blogschool.blogs.entity.BlogPostEntity;
import com.blogschool.blogs.repository.BlogPostRepository;
import com.blogschool.blogs.service.BlogPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.List;
@RestController
@RequestMapping("/api/blogposts")
public class BlogPostController {

    @Autowired
    private BlogPostRepository blogPostRepository;



//    public BlogPostController(BlogPostService blogPostService) {
//        this.blogPostService = blogPostService;
//    }


//    @PostMapping("/create")
//    public ResponseEntity<BlogPostEntity> createBlogPost(@RequestBody BlogPostEntity blogPost) {
//        BlogPostEntity createdPost = blogPostService.createBlogPost(blogPost);
//        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
//    }

//    @PostMapping("/create")
//    public BlogPostEntity createBlog(@RequestBody BlogPostEntity newBlogPost) {
//        return blogPostService.createBlogPost(newBlogPost);
//    }


    @GetMapping("/get")
//    public ResponseEntity<List<BlogPostEntity>> getAllBlogPosts() {
        List<BlogPostEntity> getAllBlogPost() {
            try{return blogPostRepository.findAll();}catch(Exception ex){
                Logger logger = Logger.getAnonymousLogger();
                Exception e1 = new Exception();
                Exception e2 = new Exception(e1);
                logger.log(Level.SEVERE, "an exception was thrown", e2);
            }
    return null;
        }





//        if (blogPosts != null) {
//            return new ResponseEntity<>(blogPosts, HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }

//    @GetMapping("/a")
//    public ResponseEntity<BlogPostEntity> getBlogPostById(@PathVariable Long id) {
//        BlogPostEntity blogPost = blogPostService.getBlogPostById(id);
//        if (blogPost != null) {
//            return new ResponseEntity<>(blogPost, HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }
}
