package com.blogschool.blogs.controller;

import com.blogschool.blogs.entity.ResponseObject;
import com.blogschool.blogs.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/blogPosts/tag")
public class TagController {
    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping("/view")
    public ResponseEntity<ResponseObject> viewAllTag() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject("ok", "found", tagService.viewAll()));
    }

}
