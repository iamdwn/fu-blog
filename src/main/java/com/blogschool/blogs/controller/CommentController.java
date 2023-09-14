package com.blogschool.blogs.controller;

import com.blogschool.blogs.entity.ResponeObject;
import com.blogschool.blogs.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/view")
    public ResponseEntity<ResponeObject> viewComment(@RequestParam Long postId) {
        return commentService.viewComment(postId);
    }

    @PostMapping("/insert")
    public ResponseEntity<ResponeObject> insertComment(
            @RequestParam Date createdDate,
            @RequestParam String content,
            @RequestParam Long postId,
            @RequestParam Long userId
    ) {
        return commentService.insertComment(createdDate, content, postId, userId);
    }
}
