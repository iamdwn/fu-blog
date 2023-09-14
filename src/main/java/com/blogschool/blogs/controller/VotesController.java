package com.blogschool.blogs.controller;

import com.blogschool.blogs.entity.ResponeObject;
import com.blogschool.blogs.service.VotesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/vote")
public class VotesController {
    private final VotesService votesService;

    @Autowired
    public VotesController(VotesService votesService) {
        this.votesService = votesService;
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
