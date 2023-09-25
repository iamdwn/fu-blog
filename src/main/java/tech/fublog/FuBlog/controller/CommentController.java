package tech.fublog.FuBlog.controller;

import tech.fublog.FuBlog.dto.CommentDTO;
import tech.fublog.FuBlog.model.ResponseObject;
import tech.fublog.FuBlog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth/blogPosts")
@CrossOrigin(origins = "*")
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


    @PutMapping("/editComment/{postId}")
    public ResponseEntity<ResponseObject> updateComment(
            @PathVariable Long postId,
            @RequestBody CommentDTO commentDTO) {
        return commentService.updateComment(postId, commentDTO);
    }

}
