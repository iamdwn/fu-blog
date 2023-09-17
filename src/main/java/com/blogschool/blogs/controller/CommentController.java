package com.blogschool.blogs.controller;

import com.blogschool.blogs.dto.CommentDTO;
import com.blogschool.blogs.entity.ResponseObject;
import com.blogschool.blogs.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/blogPosts/comment")
public class CommentController {
    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/view")
    public ResponseEntity<ResponseObject> viewComment(@RequestParam Long postId) {
        return commentService.viewComment(postId)/*viewComment(postId)*/;
    }

    @PostMapping("/insert")
    public ResponseEntity<ResponseObject> insertComment(@RequestBody CommentDTO commentDTO
//            @RequestParam String content,
//            @RequestParam Long postId,
//            @RequestParam Long userId
    ) {
        return commentService.insertComment(/*content, postId, userId*/commentDTO);
    }

    @PutMapping("/view/{commentId}")
    public ResponseEntity<ResponseObject> updateComment(@PathVariable Long commentId, @RequestBody CommentDTO commentDTO) {
        return commentService.updateComment(commentId, commentDTO);
    }
}
