package com.blogschool.blogs.controller;

import com.blogschool.blogs.entity.ResponeObject;
import com.blogschool.blogs.service.VotesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/vote")
public class VotesController {
    private final VotesService votesService;

    @Autowired
    public VotesController(VotesService votesService) {
        this.votesService = votesService;
    }

    @GetMapping("/view")
    public ResponseEntity<ResponeObject> viewVotes(@RequestParam Long postId) {
        return votesService.viewVotes(postId);
    }

    @PostMapping("/insert")
    public ResponseEntity<ResponeObject> insertVotes(
            @RequestParam Long voteValue,
            @RequestParam Long postId,
            @RequestParam Long userId
    ) {
        return votesService.insertVotes(voteValue, postId, userId);
    }
}
