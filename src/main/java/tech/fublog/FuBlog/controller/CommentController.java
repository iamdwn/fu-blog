package tech.fublog.FuBlog.controller;

import tech.fublog.FuBlog.Utility.TokenChecker;
import tech.fublog.FuBlog.dto.request.CommentRequestDTO;
import tech.fublog.FuBlog.dto.response.CommentResponseDTO;
import tech.fublog.FuBlog.model.ResponseObject;
import tech.fublog.FuBlog.exception.CommentException;
import tech.fublog.FuBlog.service.AwardService;
import tech.fublog.FuBlog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth/blogPosts/comment")
@CrossOrigin(origins = {"http://localhost:5173", "https://fublog.tech"})
//@CrossOrigin(origins = "*")
public class CommentController {
    private final CommentService commentService;
    private final AwardService awardService;

    @Autowired
    public CommentController(CommentService commentService, AwardService awardService) {
        this.commentService = commentService;
        this.awardService = awardService;
    }

    @PostMapping("/insert")
    public ResponseEntity<ResponseObject> insertComment(@RequestHeader("Authorization") String token,
                                                        @RequestBody CommentResponseDTO commentDTO) {
        try {
            if (TokenChecker.checkToken(token)) {
                commentService.insertComment(commentDTO);
                awardService.checkAward(commentDTO.getUserId());
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseObject("ok", "Comment have been inserted", ""));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", "not found", ""));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }

    }

    @GetMapping("/view/{postId}")
    public ResponseEntity<ResponseObject> viewComment(@PathVariable Long postId) {
        try {
            List<CommentResponseDTO> dtoList = commentService.viewComment(postId);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("ok", "found", dtoList));
        } catch (CommentException ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseObject> updateComment(@RequestHeader("Authorization") String token,
                                                        @RequestBody CommentRequestDTO commentRequestDTO) {
        try {
            if (TokenChecker.checkToken(token)) {
                commentService.updateComment(commentRequestDTO);
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseObject("ok", "Comment have been updated", ""));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", "not found", ""));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseObject> deleteComment(@RequestHeader("Authorization") String token,
                                                        @RequestBody CommentRequestDTO commentRequestDTO) {
        try {
            if (TokenChecker.checkToken(token)) {
                commentService.deleteComment(commentRequestDTO);
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseObject("ok", "Comment have been deleted", ""));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", "not found", ""));
        } catch (RuntimeException ex) {
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

}
