package com.blogschool.blogs.controller;

import com.blogschool.blogs.entity.ResponeEntity;
import com.blogschool.blogs.service.BlogPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/blog")
public class SearchBlogController {
    @Autowired
    private final BlogPostService blogPostService;

    public SearchBlogController(BlogPostService blogPostService) {
        this.blogPostService = blogPostService;
    }

    @GetMapping(path = "/{id}")
    ResponseEntity<ResponeEntity> findBlogByCategory(@PathVariable String id) {
        return blogPostService.findBlogByCategory(id);
    }
}
