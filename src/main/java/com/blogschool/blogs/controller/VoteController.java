package com.blogschool.blogs.controller;

import com.blogschool.blogs.model.ResponseObject;
import com.blogschool.blogs.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/blogPosts")
public class VoteController {
    private final VoteService voteService;

    @Autowired
    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @GetMapping("/viewVote/{postId}")
    public ResponseEntity<ResponseObject> viewVote(@PathVariable Long postId) {
        return voteService.viewVotes(postId);
    }

    @PostMapping("/insertVote/{postId}")
    public ResponseEntity<ResponseObject> insertVote(
            @PathVariable Long postId,
            @RequestParam Long voteValue,
            @RequestParam Long userId
    ) {

        return voteService.insertVotes(voteValue, postId, userId);
    }

}
