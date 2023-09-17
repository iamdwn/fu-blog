package com.blogschool.blogs.controller;

import com.blogschool.blogs.dto.VoteDTO;
import com.blogschool.blogs.entity.ResponseObject;
import com.blogschool.blogs.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/blogPosts/vote")
public class VoteController {
    private final VoteService voteService;

    @Autowired
    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @GetMapping("/view")
    public ResponseEntity<ResponseObject> viewVote(@RequestParam Long postId) {
        return voteService.viewVote(postId);
    }

    @PostMapping("/insert")
    public ResponseEntity<ResponseObject> insertVote(@RequestBody VoteDTO voteDTO
//            @RequestParam Long voteValue,
//            @RequestParam Long postId,
//            @RequestParam Long userId
    ) {
        return voteService.upsertVote(voteDTO);
    }

//    @PutMapping("/view/{voteId}")
//    public ResponseEntity<ResponseObject> updateVote(@PathVariable Long voteId, @RequestBody VoteDTO voteDTO) {
//        return voteService.updateVote(voteId, voteDTO);
//    }
}
