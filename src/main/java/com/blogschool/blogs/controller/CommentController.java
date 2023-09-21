package com.blogschool.blogs.controller;

import com.blogschool.blogs.dto.CommentDTO;
import com.blogschool.blogs.entity.CommentEntity;
import com.blogschool.blogs.entity.ResponseObject;
import com.blogschool.blogs.exception.CommentException;
import com.blogschool.blogs.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blogPosts/comment")
public class CommentController {
    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseObject> deleteComment(@RequestBody CommentDTO commentDTO) {
        try {
            commentService.deleteComment(commentDTO);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("ok", "Comment have been deleted", ""));
        } catch (CommentException ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }

    @GetMapping("/view/{postId}")
    public ResponseEntity<ResponseObject> viewComment(@PathVariable Long postId) {
        try {
            List<CommentDTO> dtoList = commentService.viewComment(postId);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("ok", "found", dtoList));
        } catch (CommentException ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }

    @GetMapping("/count/{postId}")
    public ResponseEntity<ResponseObject> countComment(@PathVariable Long postId) {
        try {
            Long count = commentService.countComment(postId);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("ok", "found", count));
        } catch (CommentException ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }

    @PostMapping("/insert")
    public ResponseEntity<ResponseObject> insertComment(@RequestBody CommentDTO commentDTO) {
        try {
            commentService.insertComment(commentDTO);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("ok", "Comment have been inserted", ""));
        } catch (CommentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }

    @PutMapping("/view/update")
    public ResponseEntity<ResponseObject> updateComment(@RequestBody CommentDTO commentDTO) {
        try {
            commentService.updateComment(commentDTO);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("ok", "Comment have been updated", ""));
        } catch (CommentException ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }
}
