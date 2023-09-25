package tech.fublog.FuBlog.controller;

import tech.fublog.FuBlog.dto.VoteDTO;
import tech.fublog.FuBlog.model.ResponseObject;
import tech.fublog.FuBlog.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth/blogPosts")
@CrossOrigin(origins = "*")
public class VoteController {
    private final VoteService voteService;

    @Autowired
    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @GetMapping("/viewVote/{postId}")
    public ResponseEntity<ResponseObject> viewVote(@PathVariable Long postId)
    {

        return voteService.viewVotes(postId);
    }


    @PostMapping("/insertVote/{postId}")
    public ResponseEntity<ResponseObject> insertVote(
            @PathVariable Long postId,
            @RequestBody VoteDTO voteDTO
    ) {

        return voteService.insertUpdateVotes(postId, voteDTO);
    }

}
