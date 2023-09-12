package com.blogschool.blogs.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/blog")
public class SearchBlogController {
    @GetMapping(path = "/")
    String getString(){
        return "hello";
    }
}
