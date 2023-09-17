package com.blogschool.blogs.controller;

import com.blogschool.blogs.dto.CommentDTO;
import com.blogschool.blogs.model.ResponseObject;
import com.blogschool.blogs.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/blogPosts")
public class CommentController {
    private CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/viewComment/{postId}")
    public ResponseEntity<ResponseObject> viewComment(@PathVariable Long postId) {
        return commentService.viewComment(postId);
    }


    @PostMapping("/insertComment/{postId}")
    public ResponseEntity<ResponseObject> insertComment(
            @PathVariable Long postId,
            @RequestBody CommentDTO commentDTO) {
        return commentService.insertComment(postId, commentDTO);
    }

}
