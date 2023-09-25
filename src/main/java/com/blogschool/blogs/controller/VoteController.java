package com.blogschool.blogs.controller;

import com.blogschool.blogs.dto.VoteDTO;
import com.blogschool.blogs.entity.ResponseObject;
import com.blogschool.blogs.exception.VoteException;
import com.blogschool.blogs.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blogPosts/vote")
public class VoteController {
    private final VoteService voteService;

    @Autowired
    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @GetMapping("/view/{postId}")
    public ResponseEntity<ResponseObject> viewVote(@PathVariable Long postId) {
        try {
            List<VoteDTO> dtoList = voteService.viewVote(postId);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("ok", "found", dtoList));
        } catch (VoteException ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }

    @GetMapping("/count/{postId}")
    public ResponseEntity<ResponseObject> countVote(@PathVariable Long postId) {
        try {
            Long count = voteService.countVote(postId);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("ok", "found", count));
        } catch (VoteException ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("failed", ex.getMessage(), ""));
        }
    }

    @PostMapping("/insert")
    public ResponseEntity<ResponseObject> insertVote(@RequestBody VoteDTO voteDTO) {
        return voteService.upsertVote(voteDTO);
    }

}
